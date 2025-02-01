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
body {
	margin: 0;
	width: 100%;
	height: 98vh;
}

.base {
	display: flex;
	flex-direction: column;
	height: 100%;
}

.header-container {
	height: 80px;
}

.middle-container {
	display: flex;
	flex-direction: row;
	height: 100%;
}

.menu-container {
	height: 100%;
}

.content-container {
	width:100%;
	height: 100%;
}
</style>
</head>
<body>
	<div class="base">
		<div class="header-container">
			<h1><%=Constants.APP_NAME%></h1>
		</div>
		<div class="middle-container">
			<div class="menu-container">
				<iframe src="mainMenu" width="180px" height="100%"></iframe>
			</div>
			<div class="content-container">
				<iframe src="taskList" width="100%" height="100%"></iframe>
			</div>
		</div>
	</div>
</body>
</html>