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
}

.base {
	display: flex;
	flex-direction: column;
	height: 100vh;
}

.header-container {
	height: 80px;
}

.middle-container {
	display: flex;
	flex-direction: row;
	height: 100%;
}

#meneFrame {
	width: 180px;
	height: 100%;
}

#contentFrame {
	flex: 1;
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
			<iframe id="menuFrame" src="mainMenu" name="menuFrame"></iframe>
			<iframe id="contentFrame" src="taskList" name="contentFrame"></iframe>
		</div>
	</div>
</body>
</html>