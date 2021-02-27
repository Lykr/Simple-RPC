package com.learning.properties;

import lombok.Data;

/**
 * Service properties for service discovery and registration
 */
@Data
public class ServiceProperties {
    private String name;
    private String version;

    public String getRpcServiceName() {
        return this.getName() + this.getVersion();
    }
}
