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
<script type="text/javascript">
  function showConfirm(name) {
    var result = confirm("以下のユーザーを削除してもよろしいですか？。\n"+name);
    return result;
  }
</script>
</head>
<body>
<h1><%= Constants.APP_NAME %></h1>
<h2>ユーザー一覧</h2>
  <table border="1">
    <tr>
      <th>ID</th>
      <th>名前</th>
      <th>Email</th>
      <th>Role</th>
      <th>編集</th>
      <th>削除</th>
    </tr>
    <c:forEach var="user" items="${userList}">
      <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.email}</td>
        <td>${user.roleName}</td>
        <td>
          <form action="userEdit" method="post">
            <input type="hidden" name="id" value="${user.id}">
            <button type="submit">編集</button>
          </form>
        </td>
        <td>
          <form action="userDelete" method="post" onSubmit="return showConfirm('${user.name}')")>
            <input type="hidden" name="id" value="${user.id}">
            <button type="submit">削除</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
  <form action="userAdd" method="post">
    <button type="submit">ユーザー新規追加</button>
  </form>
  <br>
  <a href="mainMenu">戻る</a>
</body>
</html>