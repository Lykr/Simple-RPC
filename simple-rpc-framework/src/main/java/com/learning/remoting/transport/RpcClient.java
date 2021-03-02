package com.learning.remoting.transport;

import com.learning.remoting.dto.RpcRequest;

public interface RpcClient {
    /**
     * Remote procedure call
     *
     * @param request rpc request
     * @return result object
     */
    public abstract Object call(RpcRequest request);
}
