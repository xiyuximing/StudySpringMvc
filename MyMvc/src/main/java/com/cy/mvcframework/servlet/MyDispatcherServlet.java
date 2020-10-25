package com.cy.mvcframework.servlet;

import com.cy.mvcframework.anno.*;
import com.cy.mvcframework.interceptor.MyInterceptor;
import com.cy.mvcframework.pojo.Handler;
import com.cy.mvcframework.pojo.HandlerExecutionChain;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;

public class MyDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    /**
     * 缓存扫描到的类名
     */
    private List<String> classNames = new ArrayList<>();

    public Map<String, Object> ioc = new HashMap<>();

    public List<Handler> handlerMapping = new ArrayList<>();
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
                url = baseUrl + url;
                Handler handler = new Handler(method, entry.getValue(), Pattern.compile(url));
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    if (parameter.getType() == HttpServletRequest.class || parameter.getType()  == HttpServletResponse.class) {
                        //为httpServletRequest及HttpServletResponse时，存入类型名称，方便后续处理
                        handler.getParamterIndex().put(parameter.getType().getSimpleName(), i);
                    } else {
                        handler.getParamterIndex().put(parameter.getName(), i);
                    }
                }
                handlerMapping.add(handler);
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
            ioc.put(properties.get("interceptor").toString(), Class.forName(properties.get("interceptor").toString()).newInstance());
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
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandlerExecutionChain chain = getHandlerChain(req);
        if (chain == null) {
            resp.getWriter().write("404");
            return;
        }
        if (!chain.applyPreHandle(req,resp)) {
            req.setAttribute("name", "无权限");

            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }
        Handler handler = chain.getHandler();
        Method method = handler.getMethod();
        Map<String, Integer> indexMap = handler.getParamterIndex();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        Map<String, String[]> reqParamterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> reqParameter : reqParamterMap.entrySet()) {
            if (indexMap.containsKey(reqParameter.getKey())) {
                Integer index = indexMap.get(reqParameter.getKey());
                Class<?> type = parameterTypes[index];
                if (type == String.class) {
                    args[index] = StringUtils.join(reqParameter.getValue(), ",");
                }
            }
        }
        if (indexMap.get(HttpServletRequest.class.getSimpleName()) != null) {
            args[indexMap.get(HttpServletRequest.class.getSimpleName())] = req;
        }
        if (indexMap.get(HttpServletResponse.class.getSimpleName()) != null) {
            args[indexMap.get(HttpServletResponse.class.getSimpleName())] = resp;
        }
        try {
            method.invoke(handler.getController(), args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private HandlerExecutionChain getHandlerChain(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Handler ha = null;
        for (Handler handler : handlerMapping) {
            if (handler.getPattern().matcher(uri).find()) {
                ha = handler;
            }
        }
        if (ha == null) {
            return null;
        }
        HandlerExecutionChain chain = new HandlerExecutionChain(ha);
        if (ha.getMethod().isAnnotationPresent(Security.class) || ha.getController().getClass().isAnnotationPresent(Security.class)) {
            chain.addInterceptor((MyInterceptor)ioc.get(properties.get("interceptor").toString()));
        }
        return chain;
    }
}
