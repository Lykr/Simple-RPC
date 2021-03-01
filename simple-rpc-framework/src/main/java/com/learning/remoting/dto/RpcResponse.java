package com.learning.remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 6590520280737965182L;
    /**
     * 200 - success
     * 400 - failure
     */
    private Integer code;
    private Object data;

    public static RpcResponse success(Object data) {
        return new RpcResponse(200, data);
    }

    public static RpcResponse failure() {
        return new RpcResponse(400, null);
    }
}
