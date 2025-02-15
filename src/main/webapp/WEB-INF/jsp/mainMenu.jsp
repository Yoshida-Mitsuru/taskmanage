<%@page import="constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="constants.Constants"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=Constants.APP_NAME%></title>
<style>
.menu-container {
	height:100%;
}
</style>
</head>
<body>
	<div class="menu-container">
	<c:choose>
		<c:when test="${not empty loginUser.id}">
			<p>ようこそ${loginUser.name}さん</p>
			<a href="logout" target="_top">ログアウト</a>
			<br>
			<a href="taskList" target="contentFrame">タスク表示</a>
			<c:if test="${loginUser.admin}">
				<p>管理者 メニュー</p>
				<a href="userList" target="contentFrame">ユーザー管理</a>
				<br>
				<a href="groupList" target="contentFrame">グループ管理</a>
				<br>
			</c:if>
		</c:when>
		<c:otherwise>
			<p>${message}</p>
      ログインしなおしてください。<br>
			<a href="index.jsp" target="_top">TOPへ</a>
		</c:otherwise>
	</c:choose>
	</div>
</body>
</html>