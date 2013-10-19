<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

	<head>
		<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
		<title>SCB- Demo</title>
	</head>

	<body>

	<h1>Aplicação demonstração de colagens</h1>
	
	<form action="https://www.facebook.com/dialog/oauth" method="GET" class="formFacebook">
		<input type="hidden" name="client_id" value=""/>
		<input type="hidden" name="redirect_uri" value="http://jm-sfcb.appspot.com:8080/Login"/>
		<input type="image" src="imagens/botao-login-facebook.png" alt="Entrar pelo Facebook">
	</form>
	
	</body>
	
</html>