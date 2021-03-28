package com.learning.remoting.transport.rabbitmq;

import com.learning.exception.NoServerException;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import com.learning.remoting.transport.AbstractClient;
import com.learning.serializer.Serializer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RpcRabbitMQClient extends AbstractClient {
    @Autowired
    Serializer serializer;

    @Override
    public Object call(RpcRequest request) {
        InetSocketAddress socketAddress = getServiceAddress(request);

        String serverAddress = socketAddress.getHostString() + ":" + socketAddress.getPort();
        Object res = null;
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(socketAddress.getHostString());
        connectionFactory.setPort(socketAddress.getPort());
        // 3. Connect server
        log.info("Connect to server {}.", serverAddress);
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            final String corrId = UUID.randomUUID().toString();
            String replyQueueName = channel.queueDeclare().getQueue();
            // 4. Send request
            AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();
            log.info("Try to send request.");
            channel.basicPublish("", "rpc_queue", props, serializer.serialize(request));
            // 5. Get response
            final BlockingQueue<RpcResponse> response = new ArrayBlockingQueue<>(1);
            String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response.offer(serializer.deserialize(delivery.getBody(), RpcResponse.class));
                }
            }, consumerTag -> {
            });
            RpcResponse result = response.take();
            channel.basicCancel(ctag);
            // 6. Parse response
            res = result.getData();
            log.info("Get response from server {} for service {}.", serverAddress, request.getServiceName());
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            log.info("Fail to connect server {}.", serverAddress);
        }
        // 7. Return result
        return res;
    }
}
