package com.learning.annotation;

import java.lang.annotation.*;

/**
 * RPC service annotation, marked on the service implementation
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RpcService {

}
