package com.cy.mvcframework.pojo;

import com.cy.mvcframework.interceptor.MyInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecutionChain {

    private List<MyInterceptor> interceptors;

    private Handler handler;

    public HandlerExecutionChain() {
    }

    public HandlerExecutionChain(Handler handler) {
        this.handler = handler;
        this.interceptors = new ArrayList<>();
    }

    public List<MyInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<MyInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) {
        if (interceptors == null || interceptors.isEmpty()) {
            return true;
        }
        for (MyInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(request, response, this.handler)) {
                return false;
            }
        }
        return true;
    }

    public void addInterceptor(MyInterceptor interceptor) {
        interceptors.add(interceptor);
    }
}
