<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%@ page import="constants.Constants" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= Constants.APP_NAME %></title>
</head>
<body>
<h1><%= Constants.APP_NAME %></h1>
<form action="login" method="post">
ユーザーID：<input type="text" name="id"><br>
パスワード：<input type="password" name="pass"><br>
<button type="submit">ログイン</button>
</form>
</body>
</html>