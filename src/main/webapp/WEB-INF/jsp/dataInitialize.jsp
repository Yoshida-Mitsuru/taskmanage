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
</head>
<body>
<h1><%= Constants.APP_NAME %></h1>
<h2>データ初期化</h2>
<c:if test="${not empty message}">
  <p><c:out value="${message}"/></p>
</c:if>
<a href="index.jsp">トップへ</a>
</body>
</html>