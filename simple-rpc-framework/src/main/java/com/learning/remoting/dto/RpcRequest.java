package com.learning.remoting.dto;

import com.learning.properties.RpcServiceProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -5411014458662297553L;
    private String requestId;
    private String interfaceName;
    private String version;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
