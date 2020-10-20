package com.cy.mvcframework.pojo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Handler {

    /**
     * 方法
     */
    private Method method;

    /**
     * controller类
     */
    private Object controller;

    /**
     * url正则
     */
    private Pattern pattern;

    /**
     * 参数顺序
     */
    private Map<String, Integer> paramterIndex = new HashMap<>();

    public Handler() {
    }

    public Handler(Method method, Object controller, Pattern pattern) {
        this.method = method;
        this.controller = controller;
        this.pattern = pattern;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamterIndex() {
        return paramterIndex;
    }

    public void setParamterIndex(Map<String, Integer> paramterIndex) {
        this.paramterIndex = paramterIndex;
    }
}
