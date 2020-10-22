package com.cy.mvcframework.interceptor;

import com.cy.mvcframework.pojo.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MyInterceptor {

    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Handler handler);
}
