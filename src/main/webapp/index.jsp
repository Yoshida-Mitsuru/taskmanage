<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%@ page import="constants.Constants" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= Constants.APP_NAME %></title>
<script type="text/javascript">
  function showConfirm() {
    var result = confirm("すべてのデータを初期化してもよろしいですか？");
    return result;
  }
</script>
</head>
<body>
<h1><%= Constants.APP_NAME %></h1>
<h2>ログインしてください</h2>
<form action="login" method="post">
ユーザーID：<input type="text" name="id" value="sa"><br>
パスワード：<input type="password" name="pass" value="sa"><br>
<button type="submit">ログイン</button>
</form>
<hr>
<h2>デバック機能</h2>
すべてのデータを初期化します。
<form action="dataInitialize" method="post" onSubmit="return showConfirm()">
  <button type="submit">初期化実行</button>
</form>
</body>
</html>