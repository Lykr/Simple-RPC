package com.learning.loadbalance.condition;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Deprecated
public class RandomLoadBalanceCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        BeanDefinition rpcClientConfig;
        try {
            rpcClientConfig = conditionContext.getRegistry().getBeanDefinition("rpcClientConfig");
        } catch (Exception e) {
            return false;
        }
        PropertyValue propertyValue = rpcClientConfig.getPropertyValues().getPropertyValue("loadBalancer");
        if (propertyValue == null) return true;
        if (propertyValue.toString().equals("random")) return true;
        return false;
    }
}
