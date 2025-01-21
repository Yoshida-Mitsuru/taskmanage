<%@page import="constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="constants.Constants" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= Constants.APP_NAME %></title>
</head>
<body>
  <h1><%= Constants.APP_NAME %></h1>
  <c:choose>
    <c:when test="${not empty loginUser.id}">
      <p>ようこそ${loginUser.name}さん</p>
      <a href="logout">ログアウト</a><br>
      <a href="Main">つぶやき投稿・閲覧へ</a>
      <c:if test="${loginUser.admin}">
        <p>管理者 メニュー</p>
        <a href="userList">ユーザー管理</a><br>
        <a href="groupList">グループ管理</a><br>
      </c:if>
    </c:when>
    <c:otherwise>
      <p>${message}</p>
      ログインしなおしてください。<br>
      <a href="index.jsp">TOPへ</a>
    </c:otherwise>
  </c:choose>
</body>
</html>