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
<h2>ユーザー情報編集</h2>
  <form action="userEditSubmit" method="post">
    <label for="id">
    ID:&nbsp;<input type="text" name="id" id="id" maxlength="10" value="${editUser.id}" readonly>
    </label><br>
    <label for="password">
    	パスワード:&nbsp;<input type="password" name="password" id="password" maxlength="10" value="${editUser.password}" required>
    </label><br>
    <label for="name">
    名前:&nbsp;<input type="text" name="name" id="name" maxlength="100" value="${editUser.name}" required>
    </label><br>
    <label for="email">
    Email:&nbsp;<input type="email" name="email" id="email" maxlength="100" value="${editUser.email}">
    </label><br>
    <label for="role">
    Role:&nbsp;
      <label for="roleAdmin">
        <input type="radio" name="role" id="roleAdmin" value="0" <c:if test="${editUser.role == 0}">checked</c:if>>管理者
      </label>
      <label for="roleUser">
        <input type="radio" name="role" id="roleUser" value="1" <c:if test="${editUser.role == 1}">checked</c:if>>ユーザー
      </label>
    </label><br>
    <button type="submit">更新</button>
  </form>
  <p>${message}</p>
  <a href="userList">ユーザー一覧に戻る</a>
</body>
</html>