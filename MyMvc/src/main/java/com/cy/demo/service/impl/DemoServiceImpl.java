package com.cy.demo.service.impl;

import com.cy.demo.service.IDemoService;
import com.cy.mvcframework.anno.MyService;

@MyService
public class DemoServiceImpl implements IDemoService {
    @Override
    public String get(String name) {
        System.out.println("name:" + name);
        return name;
    }
}
