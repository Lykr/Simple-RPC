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
    private static final long serialVersionUID = -5805202753123855881L;
    /**
     * 200 - success
     * 400 - failure
     */
    private Integer code;
    private Object data;
}
