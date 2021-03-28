package com.learning.loadbalance.condition;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Deprecated
public class RoundRobinLoadBalanceCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        BeanDefinition rpcClientConfig = null;
        try {
            rpcClientConfig = conditionContext.getRegistry().getBeanDefinition("rpcClientConfig");
        } catch (Exception e) {
            return false;
        }
        PropertyValue propertyValue = rpcClientConfig.getPropertyValues().getPropertyValue("loadBalancer");
        if (propertyValue == null) return false;
        if (propertyValue.toString().equals("robin")) return true;
        return false;
    }
}
