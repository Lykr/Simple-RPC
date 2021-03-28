package com.learning.remoting.transport.rabbitmq;

import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import com.learning.remoting.handler.RpcRequestHandler;
import com.learning.remoting.transport.AbstractServer;
import com.learning.serializer.Serializer;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RpcRabbitMQServer extends AbstractServer {
    @Autowired
    Serializer serializer;
    @Autowired
    private RpcRequestHandler rpcRequestHandler;

    @Override
    public void start() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(LOCAL_HOST_ADDRESS);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare("rpc_queue", false, false, false, null);
            channel.queuePurge("rpc_queue");

            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                RpcResponse response = null;

                try {
                    RpcRequest request = serializer.deserialize(delivery.getBody(), RpcRequest.class);
                    Object result = rpcRequestHandler.handle(request);
                    response = new RpcResponse(result, request.getRequestId());
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, serializer.serialize(response));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume("rpc_queue", false, deliverCallback, (consumerTag -> {
            }));
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
