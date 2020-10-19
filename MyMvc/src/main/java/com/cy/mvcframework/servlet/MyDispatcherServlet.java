package com.cy.mvcframework.servlet;

import com.cy.mvcframework.anno.MyAutowired;
import com.cy.mvcframework.anno.MyController;
import com.cy.mvcframework.anno.MyRequestMapping;
import com.cy.mvcframework.anno.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class MyDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    /**
     * 缓存扫描到的类名
     */
    private List<String> classNames = new ArrayList<>();

    public Map<String, Object> ioc = new HashMap<>();

    public Map<String, Method> handlerMapping = new HashMap<>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        //加载配置文件
        doLoadConfig(contextConfigLocation);
        //扫描相关的类，扫描注解
        doScan(properties.getProperty("scanPackage"));
        //初始化bean
        doInstance();
        //实现依赖注入
        doAutowired();
        //构造HandlerMapping处理器映射器，将url和method建立联系
        initHandlerMapping();
        System.out.println("springmvc初始化完成");
        //等待请求进入，处理请求
    }

    /**
     * 构造HandlerMapping处理器映射器，将url和method建立联系
     */
    private void initHandlerMapping() {
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }
            String baseUrl = "";
            MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
            if (requestMapping != null) {
                baseUrl = requestMapping.value();
            }
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }
                String url = method.getAnnotation(MyRequestMapping.class).value();
                handlerMapping.put(baseUrl + url, method);
            }
        }

    }

    private void doAutowired() {
        try {
            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
                for (Field field : entry.getValue().getClass().getDeclaredFields()) {
                    if (!field.isAnnotationPresent(MyAutowired.class)) {
                        return;
                    }
                    MyAutowired annotation = field.getAnnotation(MyAutowired.class);
                    String beanName = annotation.value();
                    if ("".equals(beanName.trim())) {
                        beanName = field.getType().getName();
                    }
                    field.setAccessible(true);
                    field.set(entry.getValue(), ioc.get(beanName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doInstance() {
        try {
            for (String className : classNames) {
                Class clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyController.class)) {
                    String beanName = clazz.getSimpleName();
                    ioc.put(lowerFirst(beanName), clazz.newInstance());
                } else if (clazz.isAnnotationPresent(MyService.class))  {
                    String beanName = lowerFirst(clazz.getSimpleName());
                    MyService annotation = (MyService) clazz.getAnnotation(MyService.class);
                    if (!"".equals(annotation.value().trim())) {
                        beanName = annotation.value();
                    }
                    ioc.put(beanName, clazz.newInstance());
                    for (Class interf : clazz.getInterfaces()) {
                        ioc.put(interf.getName(), clazz.newInstance());
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 首字母小写
     * @param beanName
     * @return
     */
    public String lowerFirst(String beanName) {
        char[] chars = beanName.toCharArray();
        if ('A' <= chars[0] && 'Z' >= chars[0]) {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }
    //扫描类
    private void doScan(String scanPackage) {
        String packageName = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/" + scanPackage.replaceAll("\\.", "/");
        File parentFile = new File(packageName);
        for (File file : parentFile.listFiles()) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName().replaceAll("/", "\\."));
            } else if (file.isFile() && file.getName().endsWith(".class")) {
                classNames.add(scanPackage + "." + file.getName().replaceAll(".class", ""));
            }
        }
    }

    //加载配置
    private void doLoadConfig(String contextConfigLocation) {

        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
