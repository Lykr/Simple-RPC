package com.learning.remoting.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -1559336518566033846L;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String requestId;

    public String getServiceName() {
        return interfaceName + ":" + version;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", version='" + version + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
