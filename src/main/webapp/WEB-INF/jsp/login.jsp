<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%@ page import="constants.Constants" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= Constants.APP_NAME %></title>
</head>
</head>
<body>
<h1><%= Constants.APP_NAME %></h1>
<h2>ログイン失敗</h2>
<p>${message}</p>
ログインしなおしてください。<br>
<a href="index.jsp">TOPへ</a>
</body>
</html>