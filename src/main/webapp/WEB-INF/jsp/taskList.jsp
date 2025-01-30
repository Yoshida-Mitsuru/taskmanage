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
    var result = confirm("以下のタスクを削除してもよろしいですか？\n"+id+" : "+name);
    return result;
  }
</script>
</head>
<body>
<h2>タスク一覧</h2>
  <table border="1">
    <tr>
      <th>ID</th>
      <th>件名</th>
      <th>内容</th>
      <th>投稿者</th>
      <th>完了予定日</th>
      <th>完了日</th>
      <th>対象グループ</th>
      <th>状態</th>
      <th>優先度</th>
    </tr>
    <c:forEach var="task" items="${taskList}">
      <tr>
        <td>${task.id}</td>
        <td>${task.subject}</td>
        <td>${task.content}</td>
        <td>${task.userName}</td>
        <td>${task.expectDate}</td>
        <td>${task.endDate}</td>
        <td>${task.groupName}</td>
        <td>${task.status}</td>
        <td>${task.priority}</td>
        <td>
          <form action="taskEdit" method="post">
            <input type="hidden" name="id" value="${task.id}">
            <button type="submit">編集</button>
          </form>
        </td>
        <td>
          <form action="taskDelete" method="post" onSubmit="return showConfirm('${task.id}', '${task.subject}')">
            <input type="hidden" name="id" value="${task.id}">
            <button type="submit">削除</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
  <form action="taskAdd" method="post">
    <button type="submit">タスク新規追加</button>
  </form>
  <br>
  <c:if test="${not empty message}">
    <p><c:out value="${message}"/></p>
  </c:if>
</body>
</html>