# SpringMvc

## SpringMvc应用

### SpringMVC简介

#### 1、MVC体系结构

**经典三层**

- 表现层：接口客户端请求，进行结果展示

- 业务层:业务逻辑处理

- 持久层（dao层）：数据持久化

**MVC模式**

​	表现层框架

- M model模型（数据模型【pojio、vo、po】+业务模型）
- V view视图（jsp、html）
- C controller控制器（servlet）

#### 2、SpringMVC

通过一套注解，让一个简单的Java类成为处理请求的控制器，无需实现任何接口。还支持RESTFful编程风格的请求。

SpringMvc本质是对servlet的封装，简化了serlvet的开发。

作用：

1）接收请求

2）返回响应，跳转页面

**SpringMvc全局只有一个DispatcherServlet接收所有请求，然后进行分发。原生servlet模式一个模块需要开发一个servlet。**

![image-20201018141919364](https://gitee.com/xiyuximing/image/raw/master/image-20201018141919364.png)

SpringMvc大概处理模式

![image-20201018142158198](https://gitee.com/xiyuximing/image/raw/master/image-20201018142158198.png)

### SpringMvc工作流程

#### 1、开发流程

1. 在web.xml中配置DispatcherServlet前端控制器

   ``` xml
     <servlet>
       <servlet-name>springmvc</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
       <init-param>
         <param-name>contextConfigLocation</param-name>
         <param-value>classpath:springmvc.xml</param-value>
       </init-param>
     </servlet>
     <servlet-mapping>
       <servlet-name>springmvc</servlet-name>
       <!--
         方式一：带后缀 *.action
         方式二：/ 不会拦截jsp
         方式三：/* 拦截jsp
       -->
       <url-pattern>/</url-pattern>
     </servlet-mapping>
   ```

2. 开发handler

   @requestMapping，@Controller，ModelAndView

3. 配置springmvc.xml

   ``` xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
                               http://www.springframework.org/schema/beans/spring-beans.xsd
                               http://www.springframework.org/schema/context
                               http://www.springframework.org/schema/context/spring-context.xsd
                               http://www.springframework.org/schema/mvc
                               http://www.springframework.org/schema/mvc/spring-mvc.xsd">
   
       <!--开启包扫描-->
       <context:component-scan base-package="com.cy.study.controller"/>
   
       <!--配置springmvc视图解析器-->
       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
           <property name="prefix" value="/WEB-INF/jsp/"/>
           <property name="suffix" value=".jsp"/>
       </bean>
   
       <!--自动注册最合适的处理器映射器，处理器适配器-->
       <mvc:annotation-driven/>
   </beans>
   ```

4. 将xml⽂件路径告诉springmvc（DispatcherServlet）

#### 2、SpringMvc请求处理流程

![image-20201018152039470](https://gitee.com/xiyuximing/image/raw/master/image-20201018152039470.png)

第⼀步：⽤户发送请求⾄前端控制器DispatcherServlet
第⼆步：DispatcherServlet收到请求调⽤HandlerMapping处理器映射器
第三步：处理器映射器根据请求Url找到具体的Handler（后端控制器），⽣成处理器对象及处理器拦截
器(如果 有则⽣成)⼀并返回DispatcherServlet
第四步：DispatcherServlet调⽤HandlerAdapter处理器适配器去调⽤Handler
第五步：处理器适配器执⾏Handler
第六步：Handler执⾏完成给处理器适配器返回ModelAndView
第七步：处理器适配器向前端控制器返回 ModelAndView，ModelAndView 是SpringMVC 框架的⼀个
底层对 象，包括 Model 和 View
第⼋步：前端控制器请求视图解析器去进⾏视图解析，根据逻辑视图名来解析真正的视图。
第九步：视图解析器向前端控制器返回View
第⼗步：前端控制器进⾏视图渲染，就是将模型数据（在 ModelAndView 对象中）填充到 request 域
第⼗⼀步：前端控制器向⽤户响应结果

#### 3、Spring MVC 九⼤组件

- **HandlerMapping（处理器映射器）**
  HandlerMapping 是⽤来查找 Handler 的，也就是处理器，具体的表现形式可以是类，也可以是⽅法。⽐如，标注了@RequestMapping的每个⽅法都可以看成是⼀个Handler。Handler负责具体实际的请求处理，在请求到达后，HandlerMapping 的作⽤便是找到请求相应的处理器Handler 和 Interceptor.

- **HandlerAdapter（处理器适配器）**

  HandlerAdapter 是⼀个适配器。因为 Spring MVC 中 Handler 可以是任意形式的，只要能处理请求即可。但是把请求交给 Servlet 的时候，由于 Servlet 的⽅法结构都是doService(HttpServletRequest req,HttpServletResponse resp)形式的，要让固定的 Servlet 处理⽅法调⽤ Handler 来进⾏处理，便是 HandlerAdapter 的职责。

- HandlerExceptionResolver
  HandlerExceptionResolver ⽤于处理 Handler 产⽣的异常情况。它的作⽤是根据异常设置ModelAndView，之后交给渲染⽅法进⾏渲染，渲染⽅法会将 ModelAndView 渲染成⻚⾯。
- **ViewResolver**
  ViewResolver即视图解析器，⽤于将String类型的视图名和Locale解析为View类型的视图，只有⼀个resolveViewName()⽅法。从⽅法的定义可以看出，Controller层返回的String类型视图名viewName 最终会在这⾥被解析成为View。View是⽤来渲染⻚⾯的，也就是说，它会将程序返回的参数和数据填⼊模板中，⽣成html⽂件。ViewResolver 在这个过程主要完成两件事情：ViewResolver 找到渲染所⽤的模板（第⼀件⼤事）和所⽤的技术（第⼆件⼤事，其实也就是找到视图的类型，如JSP）并填⼊参数。默认情况下，Spring MVC会⾃动为我们配置⼀个InternalResourceViewResolver,是针对 JSP 类型视图的。
- RequestToViewNameTranslator
  RequestToViewNameTranslator 组件的作⽤是从请求中获取 ViewName.因为 ViewResolver 根据ViewName 查找 View，但有的 Handler 处理完成之后,没有设置 View，也没有设置 ViewName，便要通过这个组件从请求中查找 ViewName。
- LocaleResolver
  ViewResolver 组件的 resolveViewName ⽅法需要两个参数，⼀个是视图名，⼀个是 Locale。LocaleResolver ⽤于从请求中解析出 Locale，⽐如中国 Locale 是 zh-CN，⽤来表示⼀个区域。这个组件也是 i18n 的基础。
- ThemeResolver
  ThemeResolver 组件是⽤来解析主题的。主题是样式、图⽚及它们所形成的显示效果的集合。Spring MVC 中⼀套主题对应⼀个 properties⽂件，⾥⾯存放着与当前主题相关的所有资源，如图⽚、CSS样式等。创建主题⾮常简单，只需准备好资源，然后新建⼀个“主题名.properties”并将资
  源设置进去，放在classpath下，之后便可以在⻚⾯中使⽤了。SpringMVC中与主题相关的类有ThemeResolver、ThemeSource和Theme。ThemeResolver负责从请求中解析出主题名，ThemeSource根据主题名找到具体的主题，其抽象也就是Theme，可以通过Theme来获取主题和具体的资源。
- MultipartResolver
  MultipartResolver ⽤于上传请求，通过将普通的请求包装成 MultipartHttpServletRequest 来实现。MultipartHttpServletRequest 可以通过 getFile() ⽅法 直接获得⽂件。如果上传多个⽂件，还可以调⽤ getFileMap()⽅法得到Map<FileName，File>这样的结构，MultipartResolver 的作⽤就是封装普通的请求，使其拥有⽂件上传的功能。
- FlashMapManager
  FlashMap ⽤于重定向时的参数传递，⽐如在处理⽤户订单时候，为了避免重复提交，可以处理完post请求之后重定向到⼀个get请求，这个get请求可以⽤来显示订单详情之类的信息。这样做虽然可以规避⽤户重新提交订单的问题，但是在这个⻚⾯上要显示订单的信息，这些数据从哪⾥来获得呢？因为重定向时么有传递参数这⼀功能的，如果不想把参数写进URL（不推荐），那么就可以通过FlashMap来传递。只需要在重定向之前将要传递的数据写⼊请求（可以通过ServletRequestAttributes.getRequest()⽅法获得）的属性OUTPUT_FLASH_MAP_ATTRIBUTE中，这样在重定向之后的Handler中Spring就会⾃动将其设置到Model中，在显示订单信息的⻚⾯上就可以直接从Model中获取数据。FlashMapManager 就是⽤来管理 FalshMap 的。

#### 4、url-pattern配置及原理

在web.xml中配置url-pattern

``` xml
<url-pattern>/</url-pattern>
```

- 方式一：

  带后缀，比如*.action  *.do *.aaa

  该种方式比较精确、方便

- 方式二：

  配置为/。

  该方式不会拦截 .jsp，但是会拦截.html等静态资源。

  **原理：**因为tomcat容器中有一个web.xml（父），你的项目中也有一个web.xml（子），是一个继承关系。父web.xml中有一个DefaultServlet,  url-pattern 是一个 /。此时我们自己的web.xml中也配置了一个 / ,覆写了父web.xml的配置。同时父web.xml中有一个JspServlet，这个servlet拦截.jsp文件，而我们并没有覆写这个配置，所以springmvc此时不拦截jsp，jsp的处理交给了tomcat

  **解决方式：**

  1. 在springmvc.xml中增加配置

     ``` xml
     <mvc:default-servlet-handler/>
     ```

     添加该标签配置之后，会在SpringMVC上下文中定义一个DefaultServletHttpRequestHandler对象。这个对象如同一个检查人员，对进入DispatcherServlet的url请求进行过滤筛查，如果发现是一个静态资源请求，那么会把请求转由web应用服务器（tomcat）默认的DefaultServlet来处理，如果不是静态资源请求，那么继续由SpringMVC框架处理。

     缺点：静态资源只能放置在webapp根目录下，不能放在其他位置，会报404错误

  2. 在springmvc.xml中增加配置

     ``` xml
     <mvc:resources location="classpath:/"  mapping="/resources/**"/>
     ```

     SpringMVC框架自己处理静态资源。mapping:约定的静态资源的url规则，location：指定的静态资源的存放位置

- 方式三：

  /* 拦截所有，包括.jsp

#### 5、数据输出Model、Map、ModelMap

SpringMVC在handler方法上传入Map、Model和ModelMap参数，并向这些参数中保存数据（放入到请求域），都可以在页面获取到。

``` java
    /**
     * 直接声明形参ModelMap，封装数据
     * url: http://localhost:8080/demo/handle11
     *
     * =================modelmap:class org.springframework.validation.support.BindingAwareModelMap
     */
    @RequestMapping("/handle11")
    public String handle11(ModelMap modelMap) {
        Date date = new Date();// 服务器时间
        modelMap.addAttribute("date",date);
        System.out.println("=================modelmap:" + modelMap.getClass());
        return "success";
    }


    /**
     * 直接声明形参Model，封装数据
     * url: http://localhost:8080/demo/handle12
     * =================model:class org.springframework.validation.support.BindingAwareModelMap
     */
    @RequestMapping("/handle12")
    public String handle12(Model model) {
        Date date = new Date();
        model.addAttribute("date",date);
        System.out.println("=================model:" + model.getClass());
        return "success";
    }


    /**
     * 直接声明形参Map集合，封装数据
     * url: http://localhost:8080/demo/handle13
     * =================map:class org.springframework.validation.support.BindingAwareModelMap
     */
    @RequestMapping("/handle13")
    public String handle13(Map<String,Object> map) {
        Date date = new Date();
        map.put("date",date);
        System.out.println("=================map:" + map.getClass());
        return "success";
    }

```

Map、Model、ModelMap运行时的具体类型都是BindingAwareModelMap，相当于给BindingAwareModelMap中保存的数据都会放在请求域中。

**BindingAwareModelMap**继承了**ExtendedModelMap**，ExtendedModelMap继承了ModelMap,实现了Model接口。

所以使用Map、Model、ModelMap都可以。

### 请求参数绑定

- 默认⽀持 Servlet API 作为⽅法参数

  当需要使⽤HttpServletRequest、HttpServletResponse、HttpSession等原⽣servlet对象时，直
  接在handler⽅法中形参声明使⽤即可

- 绑定简单类型参数

  参数类型推荐使⽤包装数据类型，因为基础数据类型不可以为null
  整型：Integer、int
  字符串：String
  单精度：Float、float
  双精度：Double、double
  布尔型：Boolean、boolean
  说明：**对于布尔类型的参数，请求的参数值为true或false。或者1或0**
  注意：绑定简单数据类型参数，只需要直接声明形参即可（形参参数名和传递的参数名要保持⼀
  致，建议 使⽤包装类型，当形参参数名和传递参数名不⼀致时可以使⽤@RequestParam注解进⾏
  ⼿动映射）

- 绑定Pojo类型参数

  接收pojo类型参数，直接形参声明即可，类型就是Pojo的类型，形参名⽆所谓。**但是要求传递的参数名必须和Pojo的属性名保持⼀致**

- 绑定Pojo包装对象参数

  传参参数名和pojo属性保持⼀致，如果不能够定位数据项，那么通过属性名 + "." 的⽅式进⼀步锁定数据

- 绑定⽇期类型参数

  1. ⾃定义类型转换器

     ``` java
     public class DateConverter implements Converter<String, Date> {
       @Override
       public Date convert(String source) {
         // 完成字符串向⽇期的转换
         SimpleDateFormat simpleDateFormat = new
         SimpleDateFormat("yyyy-MM-dd");
         try {
         	Date parse = simpleDateFormat.parse(source);
         	return parse;
         } catch (ParseException e) {
         	e.printStackTrace();
         }
         return null;
       }
     }
     ```

  2. 在springmvc.xml中注册⾃定义类型转换器

     ``` xml
     <!--
     ⾃动注册最合适的处理器映射器，处理器适配器(调⽤handler⽅法)
     -->
     <mvc:annotation-driven conversionservice="conversionServiceBean"/>
     <!--注册⾃定义类型转换器-->
     <bean id="conversionServiceBean" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
       <property name="converters">
         <set>
         	<bean class="com.cy.study.converter.DateConverter">
         </bean>
       </set>
     </property>
     </bean>
     ```

### restful风格支持

#### 1、什么是RESTful

REST（英⽂：Representational State Transfer，简称 REST）描述了⼀个架构样式的⽹络系统， ⽐如web 应⽤程序。它⾸次出现在 2000 年 Roy Fielding 的博⼠论⽂中，他是 HTTP 规范的主要编写者之⼀。在⽬前主流的三种 Web 服务交互⽅案中，REST 相⽐于 SOAP（Simple Object Access protocol，简单对象访问协议）以及 XML-RPC 更加简单明了，⽆论是对 URL 的处理还是对 Payload 的编码，REST 都倾向于⽤更加简单轻量的⽅法设计和实现。值得注意的是 REST 并没有⼀个明确的标准，⽽更像是⼀种设计的⻛格。

**Restful 的优点：**
它结构清晰、符合标准、易于理解、扩展⽅便

**Restful 的特性：**

资源（Resources）：⽹络上的⼀个实体，或者说是⽹络上的⼀个具体信息。
它可以是⼀段⽂本、⼀张图⽚、⼀⾸歌曲、⼀种服务，总之就是⼀个具体的存在。可以⽤⼀个 URI（统⼀资源定位符）指向它，每种资源对应⼀个特定的 URI 。要获取这个资源，访问它的 URI 就可以，因此URI 即为每⼀个资源的独⼀⽆⼆的识别符。
表现层（Representation）：把资源具体呈现出来的形式，叫做它的表现层 （Representation）。⽐如，⽂本可以⽤ txt 格式表现，也可以⽤ HTML 格式、XML 格式、JSON 格式表现，甚⾄可以采⽤⼆进制格式。
状态转化（State Transfer）：每发出⼀个请求，就代表了客户端和服务器的⼀次交互过程。HTTP 协议，是⼀个⽆状态协议，即所有的状态都保存在服务器端。因此，如果客户端想要操作服务器， 必须通过某种⼿段，让服务器端发⽣“状态转化”（State Transfer）。⽽这种转化是建⽴在表现层之上的，所以就是 “ 表现层状态转化” 。具体说， 就是 HTTP 协议⾥⾯，四个表示操作⽅式的动词：GET 、POST 、PUT 、DELETE 。它们分别对应四种基本操作：GET ⽤来获取资源，POST ⽤来新建资源，PUT ⽤来更新资源，DELETE ⽤来删除资源。

**RESTful 的示例：**

rest中，认为互联⽹中的所有东⻄都是资源，既然是资源就会有⼀个唯⼀的uri标识它，代表它http://localhost:8080/user/3 代表的是id为3的那个⽤户记录（资源）
**get 查询，获取资源**
**post 增加，新建资源**
**put 更新**
**delete 删除资源**
rest⻛格带来的直观体现：就是传递参数⽅式的变化，参数可以在uri中了
/account/1 HTTP GET ：得到 id = 1 的 account
/account/1 HTTP DELETE：删除 id = 1 的 account
/account/1 HTTP PUT：更新 id = 1 的 account
URL：资源定位符，通过URL地址去定位互联⽹中的资源（抽象的概念，⽐如图⽚、视频、app服务
等）。
RESTful ⻛格 URL：互联⽹所有的事物都是资源，要求URL中只有表示资源的名称，没有动词。
RESTful⻛格资源操作：使⽤HTTP请求中的method⽅法put、delete、post、get来操作资源。分别对
应添加、删除、修改、查询。不过⼀般使⽤时还是 post 和 get。put 和 delete⼏乎不使⽤。
RESTful ⻛格资源表述：可以根据需求对URL定位的资源返回不同的表述（也就是返回数据类型，⽐如
XML、JSON等数据格式）。

#### 2、springmvc对restful支持

**@RequestMapping(value = "/handle/{id}",method ={RequestMethod.GET})**

**@PathVariable("id")**

``` java
@RequestMapping(value = "/handle/{id}",method ={RequestMethod.GET})
public ModelAndView handleGet(@PathVariable("id") Integer id) {
  Date date = new Date();
  ModelAndView modelAndView = new ModelAndView();
  modelAndView.addObject("date",date);
  modelAndView.setViewName("success");
  return modelAndView;
}

@RequestMapping(value = "/handle",method = {RequestMethod.POST})
public ModelAndView handlePost(String username) {
  Date date = new Date();
  ModelAndView modelAndView = new ModelAndView();
  modelAndView.addObject("date",date);
  modelAndView.setViewName("success");
  return modelAndView;
}

@RequestMapping(value = "/handle/{id}/{name}",method ={RequestMethod.PUT})
public ModelAndView handlePut(@PathVariable("id") Integerid,@PathVariable("name") String username) {
  Date date = new Date();
  ModelAndView modelAndView = new ModelAndView();
  modelAndView.addObject("date",date);
  modelAndView.setViewName("success");
  return modelAndView;
}

@RequestMapping(value = "/handle/{id}",method ={RequestMethod.DELETE})
public ModelAndView handleDelete(@PathVariable("id") Integer id) {
  Date date = new Date();
  ModelAndView modelAndView = new ModelAndView();
  modelAndView.addObject("date",date);
  modelAndView.setViewName("success");
  return modelAndView;
}
```

对于PUT及DELETE,须在form表单中配置_method属性，并配置HiddenHttpMethodFilter自动将POST类型请求转换为PUT和DELETE

``` html
<form method="post" action="/demo/handle/15/lisi">
  <input type="hidden" name="_method" value="put"/>
  <input type="submit" value="提交rest_put请求"/>
</form>
<form method="post" action="/demo/handle/15">
  <input type="hidden" name="_method" value="delete"/>
  <input type="submit" value="提交rest_delete请求"/>
</form>
```

在web.xml中配置请求⽅式过滤器（将特定的post请求转换为put和delete请求）

``` xml
<filter>
  <filter-name>hiddenHttpMethodFilter</filter-name>
  <filterclass>org.springframework.web.filter.HiddenHttpMethodFilter</filterclass>
</filter>
<filter-mapping>
  <filter-name>hiddenHttpMethodFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

### SpringMVC与json交互

@RequestBody和@ResponseBody

## SpringMVC高级应用

### 拦截器

#### 1、拦截器、过滤器、监听器的对比

- Servlet：处理Request请求和Response响应

- 过滤器（Filter）：对Request请求起到过滤的作⽤，作⽤在Servlet之前，如果配置为/*可以对所有的资源访问（servlet、js/css静态资源等）进⾏过滤处理

- 监听器（Listener）：实现了javax.servlet.ServletContextListener 接⼝的服务器端组件，它随Web应⽤的启动⽽启动，只初始化⼀次，然后会⼀直运⾏监视，随Web应⽤的停⽌⽽销毁
  作⽤⼀：做⼀些初始化⼯作，web应⽤中spring容器启动ContextLoaderListener
  作⽤⼆：监听web中的特定事件，⽐如HttpSession,ServletRequest的创建和销毁；变量的创建、销毁和修改等。可以在某些动作前后增加处理，实现监控，⽐如统计在线⼈数，利⽤HttpSessionLisener等。

- 拦截器（Interceptor）：是SpringMVC、Struts等表现层框架⾃⼰的，不会拦截jsp/html/css/image的访问等，只会拦截访问的控制器⽅法（Handler）。
  从配置的⻆度也能够总结发现：serlvet、filter、listener是配置在web.xml中的，⽽interceptor是配置在表现层框架⾃⼰的配置⽂件中的
  在Handler业务逻辑执⾏之前拦截⼀次
  在Handler逻辑执⾏完毕但未跳转⻚⾯之前拦截⼀次
  在跳转⻚⾯之后拦截⼀次

  ![image-20201018213025412](https://gitee.com/xiyuximing/image/raw/master/image-20201018213025412.png)

#### 2、拦截器执行流程

**单个拦截器：**

![image-20201018230003161](https://gitee.com/xiyuximing/image/raw/master/image-20201018230003161.png)

1）程序先执⾏preHandle()⽅法，如果该⽅法的返回值为true，则程序会继续向下执⾏处理器中的⽅法，否则将不再向下执⾏。
2）在业务处理器（即控制器Controller类）处理完请求后，会执⾏postHandle()⽅法，然后会通过DispatcherServlet向客户端返回响应。
3）在DispatcherServlet处理完请求后，才会执⾏afterCompletion()⽅法。

#### 3、多个拦截器执行流程

多个拦截器（假设有两个拦截器Interceptor1和Interceptor2，并且在配置⽂件中， Interceptor1拦截器配置在前），在程序中的执⾏流程如下图所示：

![image-20201018230214527](https://gitee.com/xiyuximing/image/raw/master/image-20201018230214527.png)

从图可以看出，当有多个拦截器同时⼯作时，它们的preHandle()⽅法会按照配置⽂件中拦截器的配置**顺序执⾏**，⽽它们的postHandle()⽅法和afterCompletion()⽅法则会按照配置顺序的反序执⾏。

#### 4、示例代码

1. 编写类，实现HandlerInterceptor接口。

``` java
/**
* ⾃定义springmvc拦截器
*/
public class MyIntercepter01 implements HandlerInterceptor {
  /**
  * 会在handler⽅法业务逻辑执⾏之前执⾏
  * 往往在这⾥完成权限校验⼯作
  * @param request
  * @param response
  * @param handler
  * @return 返回值boolean代表是否放⾏，true代表放⾏，false代表中⽌
  * @throws Exception
  */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    System.out.println("MyIntercepter01 preHandle......");
    return true;
  }
  /**
  * 会在handler⽅法业务逻辑执⾏之后尚未跳转⻚⾯时执⾏
  * @param request
  * @param response
  * @param handler
  * @param modelAndView 封装了视图和数据，此时尚未跳转⻚⾯呢，你可以在这⾥针对返回的
  数据和视图信息进⾏修改
  * @throws Exception
  */
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
  	System.out.println("MyIntercepter01 postHandle......");
  }
  /**
  * ⻚⾯已经跳转渲染完毕之后执⾏
  * @param request
  * @param response
  * @param handler
  * @param ex 可以在这⾥捕获异常
  * @throws Exception
  */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
  	System.out.println("MyIntercepter01 afterCompletion......");
  }
}
```

2. 在springmvc.xml中注册拦截器

   ``` xml
   <mvc:interceptors>
     <!--拦截所有handler-->
     <!--<bean class="com.lagou.edu.interceptor.MyIntercepter01"/>-->
     <mvc:interceptor>
     <!--配置当前拦截器的url拦截规则，**代表当前⽬录下及其⼦⽬录下的所有url-->
       <mvc:mapping path="/**"/>
       <!--exclude-mapping可以在mapping的基础上排除⼀些url拦截-->
       <!--<mvc:exclude-mapping path="/demo/**"/>-->
       <bean class="com.lagou.edu.interceptor.MyIntercepter01"/>
     </mvc:interceptor>
     <mvc:interceptor>
       <mvc:mapping path="/**"/>
       <bean class="com.lagou.edu.interceptor.MyIntercepter02"/>
     </mvc:interceptor>
   </mvc:interceptors>
   ```

### 处理multipart形式的数据

文件上传

1. 引入依赖

   ``` xml
   <dependency>
     <groupId>commons-fileupload</groupId>
     <artifactId>commons-fileupload</artifactId>
     <version>1.3.1</version>
   </dependency>
   ```

2. 配置⽂件上传解析器

   ``` xml
   <!--配置⽂件上传解析器，id是固定的multipartResolver-->
   <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
     <!--设置上传⼤⼩，单位字节-->
     <property name="maxUploadSize" value="1000000000"/>
   </bean>
   ```

3. 后台接收

   ``` java
   @RequestMapping("upload")
   public String upload(MultipartFile uploadFile, HttpServletRequest request) throws IOException {
     // ⽂件原名，如xxx.jpg
     String originalFilename = uploadFile.getOriginalFilename();
     // 获取⽂件的扩展名,如jpg
     String extendName =
     originalFilename.substring(originalFilename.lastIndexOf(".") + 1,
     originalFilename.length());
     String uuid = UUID.randomUUID().toString();
     // 新的⽂件名字
     String newName = uuid + "." + extendName;
     String realPath =
     request.getSession().getServletContext().getRealPath("/uploads");
     // 解决⽂件夹存放⽂件数量限制，按⽇期存放
     String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
     File floder = new File(realPath + "/" + datePath);
     if(!floder.exists()) {
     floder.mkdirs();
     }
     uploadFile.transferTo(new File(floder,newName));
     return "success";
   }
   ```

### 在控制器中处理异常

使用@ControllerAdvice及@ExceptionHandler(ArithmeticException.class)注解

如果@ExceptionHandler注解写在controller中，只对当前controller生效

``` java
// 可以让我们优雅的捕获所有Controller对象handler⽅法抛出的异常
@ControllerAdvice
public class GlobalExceptionResolver {
  @ExceptionHandler(ArithmeticException.class)
  public ModelAndView handleException(ArithmeticException exception,HttpServletResponse response) {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("msg",exception.getMessage());
    modelAndView.setViewName("error");
    return modelAndView;
  }
}
```

### 基于Flash属性的跨重定向请求数据传递

1. 手动拼接

   ``` java
   return "redirect:handle01?name=" + name;
   ```

   缺点：属于get请求，携带参数⻓度有限制，参数安全性也不⾼

2. 使⽤SpringMVC提供的flash属性机制

   向上下⽂中添加flash属性，框架会在session中记录该属性值，当跳转到⻚⾯之后框架会⾃动删除flash属性，不需要我们⼿动删除，通过这种⽅式进⾏重定向参数传递，参数⻓度和安全性都得到了保障。

   ``` java
   @RequestMapping("/handleRedirect")
   public String handleRedirect(String name,RedirectAttributes redirectAttributes) {
     //return "redirect:handle01?name=" + name; // 拼接参数安全性、参数⻓度都有局限
     // addFlashAttribute⽅法设置了⼀个flash类型属性，该属性会被暂存到session中，在跳转到⻚⾯之后该属性销毁
     redirectAttributes.addFlashAttribute("name",name);
     return "redirect:handle01";
   }
   ```

## 手写SpringMVC

### 主要流程

![image-20201021231042614](https://gitee.com/xiyuximing/image/raw/master/image-20201021231042614.png)

## SpringMVC源码



## SSM框架整合

1. 引入jar包

   pom.xml

   ```xml
   <dependency>
     <groupId>org.mybatis</groupId>
     <artifactId>mybatis-spring</artifactId>
     <version>2.0.3</version>
   </dependency>
   ```

   

2. 配置Spring和mybatis整合

   applicationContext.xml

   ``` xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:tx="http://www.springframework.org/schema/tx"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
                              http://www.springframework.org/schema/beans/spring-beans.xsd
                              http://www.springframework.org/schema/context
                              http://www.springframework.org/schema/context/spring-context.xsd
                              http://www.springframework.org/schema/tx
                              http://www.springframework.org/schema/tx/spring-tx.xsd
   ">
     <!--包扫描-->
     <context:component-scan base-package="com.cy.edu.mapper"/>
     <context:component-scan base-package="com.cy.edu.service"/>
     <!--数据库连接池以及事务管理都交给Spring容器来完成-->
     <!--引⼊外部资源⽂件-->
     <context:property-placeholder location="classpath:jdbc.properties"/>
     <!--第三⽅jar中的bean定义在xml中-->
     <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
       <property name="driverClassName" value="${jdbc.driver}"/>
       <property name="url" value="${jdbc.url}"/>
       <property name="username" value="${jdbc.username}"/>
       <property name="password" value="${jdbc.password}"/>
     </bean>
     <!--SqlSessionFactory对象应该放到Spring容器中作为单例对象管理
     原来mybaits中sqlSessionFactory的构建是需要素材的：SqlMapConfig.xml中的内
     容
     -->
     <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
       <!--别名映射扫描-->
       <property name="typeAliasesPackage" value="com.cy.edu.pojo"/>
       <!--数据源dataSource-->
       <property name="dataSource" ref="dataSource"/>
     </bean>
     <!--Mapper动态代理对象交给Spring管理，我们从Spring容器中直接获得Mapper的代理对
     象-->
     <!--扫描mapper接⼝，⽣成代理对象，⽣成的代理对象会存储在ioc容器中-->
     <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
       <!--mapper接⼝包路径配置-->
       <property name="basePackage" value="com.cy.edu.mapper"/>
       <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
     </bean>
     <!--事务管理-->
     <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
   		<property name="dataSource" ref="dataSource"/>
   	</bean>
     <!--事务管理注解驱动-->
   	<tx:annotation-driven transaction-manager="transactionManager"/>
   </beans>
   ```

   web.xml

   ``` xml
   <context-param>
     <param-name>contextConfigLocation</param-name>
     <param-value>classpath*:applicationContext*.xml</param-value>
   </context-param>
   <!--spring框架启动-->
   <listener>
   	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
   </listener>
   ```

3. SpringMVC

   springmvc.xml

   ``` xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
                              http://www.springframework.org/schema/beans/spring-beans.xsd
                              http://www.springframework.org/schema/context
                              http://www.springframework.org/schema/context/springcontext.xsd
                              http://www.springframework.org/schema/mvc
                              http://www.springframework.org/schema/mvc/spring-mvc.xsd
   ">
     <!--扫描controller-->
     <context:component-scan base-package="com.lagou.edu.controller"/>
     <mvc:annotation-driven/>
   </beans>
   ```

   web.xml

   ``` xml
   <!--springmvc启动-->
   <servlet>
   	<servlet-name>springmvc</servlet-name>
   	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
     <init-param>
       <param-name>contextConfigLocation</param-name>
       <param-value>classpath*:springmvc.xml</param-value>
     </init-param>
   	<load-on-startup>1</load-on-startup>
   </servlet>
   <servlet-mapping>
     <servlet-name>springmvc</servlet-name>
     <url-pattern>/</url-pattern>
   </servlet-mapping>
   ```

#### 特别注意：

Spring容器和SpringMVC容器是有层次的，Spring容器：service对象+dao对象，SpringMVC容器：controller对象，可以引用到Spring容器中的对象



## SpringDataJpa

### SpringDataJpa概述

​	Spring Data JPA 是 Spring 基于JPA 规范的基础上封装的⼀套 JPA 应⽤框架，可使开发者⽤极简的代码即可实现对数据库的访问和操作。它提供了包括增删改查等在内的常⽤功能！学习并使⽤Spring Data JPA 可以极⼤提⾼开发效率。
说明：Spring Data JPA 极⼤简化了数据访问层代码。
如何简化呢？使⽤了Spring Data JPA，我们Dao层中只需要写接⼝，不需要写实现类，就⾃动具有了增删改查、分⻚查询等⽅法。
使⽤Spring Data JPA 很多场景下不需要我们⾃⼰写sql语句

### SpringDataJpa、JPA规范、Hibernate之间的关系



### SpringDataJpa应用

