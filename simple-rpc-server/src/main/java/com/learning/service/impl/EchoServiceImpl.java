package com.learning.service.impl;

import com.learning.service.EchoService;

/**
 * Implementation of EchoService
 */
public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String s) {
        return "This is a echo of: " + s;
    }
}
