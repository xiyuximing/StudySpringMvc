<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Insert title here</title>
</head>
<body>
<%
    String name = request.getAttribute("name").toString();
    System.out.println(request.getAttribute("name"));
%>
name:${name}
</body>
</html>