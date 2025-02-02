<%@page import="constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="constants.Constants" %>
<%@ page import="model.UserBean" %>
<%
  UserBean u = (UserBean)request.getAttribute("editUser");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= Constants.APP_NAME %></title>
</head>
<body>
<h2>新規ユーザー追加</h2>
  <form action="userAddSubmit" method="post">
    <label for="id">
    ID:&nbsp;<input type="text" name="id" id="id" maxlength="10" value="${editUser.id}" required>
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
        <input type="radio" name="role" id="roleAdmin" value="<%= Constants.ROLE.ADMIN.ordinal() %>"<% if(u != null && u.getRole() == Constants.ROLE.ADMIN.ordinal()) { %> checked<% } %>>
        <%= Constants.ROLE_NAME.get(Constants.ROLE.ADMIN.ordinal()) %>
      </label>
      <label for="roleUser">
        <input type="radio" name="role" id="roleUser" value="<%= Constants.ROLE.USER.ordinal() %>"<% if(u == null || u.getRole() == Constants.ROLE.USER.ordinal()) { %> checked<% } %>>
        <%= Constants.ROLE_NAME.get(Constants.ROLE.USER.ordinal()) %>
      </label>
    </label><br>
    <button type="submit">追加登録</button>
  </form>
  <p>${message}</p>
  <a href="userList">ユーザー一覧に戻る</a>
</body>
</html>