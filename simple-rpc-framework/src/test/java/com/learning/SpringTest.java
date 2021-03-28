package com.learning;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class SpringTest {

    @Test
    public void test() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SimpleRpcConfig.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String s :
                beanDefinitionNames) {
            System.out.println(s);
        }
    }
}
