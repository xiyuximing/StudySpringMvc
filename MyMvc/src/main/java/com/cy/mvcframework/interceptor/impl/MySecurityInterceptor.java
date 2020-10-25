package com.cy.mvcframework.interceptor.impl;

import com.cy.mvcframework.anno.MyService;
import com.cy.mvcframework.anno.Security;
import com.cy.mvcframework.interceptor.MyInterceptor;
import com.cy.mvcframework.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
public class MySecurityInterceptor implements MyInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Handler handler) {
        String userName = request.getParameter("username");
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        String[] values = null;
        Method method = handler.getMethod();
        Object o = handler.getController();
        if (method.isAnnotationPresent(Security.class)) {
            values = method.getAnnotation(Security.class).value();
        } else if(o.getClass().isAnnotationPresent(Security.class)){
            values = o.getClass().getAnnotation(Security.class).value();
        }
        if (values == null || values.length == 0 || Arrays.asList(values).contains(userName)) {
            return true;
        }
        return false;
    }
}
