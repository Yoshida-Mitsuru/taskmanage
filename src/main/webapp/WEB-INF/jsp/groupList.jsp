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
  function showConfirm(id, name) {
    var result = confirm("以下のグループを削除してもよろしいですか？\n"+id+" : "+name);
    return result;
  }
</script>
</head>
<body>
<h2>グループ一覧</h2>
  <table border="1">
    <tr>
      <th>ID</th>
      <th>名前</th>
      <th>説明</th>
      <th>編集</th>
      <th>削除</th>
    </tr>
    <c:forEach var="group" items="${groupList}">
      <tr>
        <td>${group.id}</td>
        <td>${group.name}</td>
        <td>${group.description}</td>
        <td>
          <form action="groupEdit" method="post">
            <input type="hidden" name="id" value="${group.id}">
            <button type="submit">編集</button>
          </form>
        </td>
        <td>
          <form action="groupDelete" method="post" onSubmit="return showConfirm('${group.id}', '${group.name}')">
            <input type="hidden" name="id" value="${group.id}">
            <button type="submit">削除</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
  <form action="groupAdd" method="post">
    <button type="submit">グループ新規追加</button>
  </form>
  <br>
  <c:if test="${not empty message}">
    <p><c:out value="${message}"/></p>
  </c:if>
</body>
</html>