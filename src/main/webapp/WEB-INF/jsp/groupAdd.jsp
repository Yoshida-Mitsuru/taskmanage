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
<h2>新規グループ追加</h2>
  <form action="groupAddSubmit" method="post">
    <label for="id">
    ID:&nbsp;IDは自動採番されます
    </label><br>
    <label for="name">
    名前:&nbsp;<input type="text" name="name" id="name" maxlength="100" value="${editGroup.name}" required>
    </label><br>
    <label for="description">
    説明:&nbsp;<input type="description" name="description" id="description" maxlength="250" value="${editGroup.description}">
    </label><br>
    <button type="submit">追加登録</button>
  </form>
  <p>${message}</p>
  <a href="groupList">グループ一覧に戻る</a>
</body>
</html>