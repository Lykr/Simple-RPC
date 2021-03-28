package com.learning.properties;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Service properties for service discovery and registration
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RpcServiceProperties {
    private String name;
    private String version;

    public RpcServiceProperties(String name) {
        this.name = name;
        this.version = null;
    }

    public RpcServiceProperties(Class<?> clazz) {
        this.name = clazz.getName();
        this.version = null;
    }

    public RpcServiceProperties(Class<?> clazz, String version) {
        this.name = clazz.getName();
        this.version = version;
    }

    public String getRpcServiceName() {
        return this.getName() + ":" + this.getVersion();
    }
}
