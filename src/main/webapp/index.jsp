<%--
  Created by IntelliJ IDEA.
  User: 夜林蓝
  Date: 2022/11/13
  Time: 0:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>hello</title>
</head>
<body>
hello
<form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/fileUpload">
    <span><input type="file" name="file1"/></span><br>
    <span><input type="file" name="file2"/></span><br>
    <span><input type="submit"/> | <input type="reset"/></span><br>
    <span> 消息：${pageContext.request.getAttribute("msg")}</span><br>
</form>
</body>
</html>
