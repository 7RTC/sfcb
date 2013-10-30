<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

	<head>
		<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
		<title>SCB- Demo</title>
	</head>

	<body>
	
		<div id="fb-root"></div>
		<script>
			window.fbAsyncInit = function() {
				FB.init({
					appId : '${facebok.app.id}', 
					status : true, 
					cookie : true, 
					xfbml : true
				});

				FB.Event.subscribe('auth.authResponseChange',
					function(response) {
						if (response.status === 'connected') {
							setaDadosUsuario();
							var accessToken = response.authResponse.accessToken;
							var expiresIn = response.authResponse.expiresIn;
							window.top.location = '${facebook.app.site_url}' + "?accessToken=" + accessToken + "&expiresIn=" + expiresIn;
						} else if (response.status === 'not_authorized') {
							FB.login();
						} else {
							FB.login();
						}
					});
			};

			// Carrega o SDK do Facebook de modo assíncrono
			(function(d) {
				var js, id = 'facebook-jssdk', ref = d
						.getElementsByTagName('script')[0];
				if (d.getElementById(id)) {
					return;
				}
				js = d.createElement('script');
				js.id = id;
				js.async = true;
				js.src = "//connect.facebook.net/pt_BR/all.js";
				ref.parentNode.insertBefore(js, ref);
			}(document));
			
			  function setaDadosUsuario() {
				    console.log('Welcome!  Fetching your information.... ');
				    FB.api('/me', function(response) {
				      console.log('Good to see you, ' + response.name + '.');
				    });
				  }
		</script>
	
		<h1>Aplicação demonstração de colagens</h1>
		
		<div class="loginFacebook"> 
			<fb:login-button show-faces="true" max-rows="1" size="xlarge" scope="${facebook.app.permissions}">Login Facebook</fb:login-button>
		</div>
		
<%--  	<form action="https://www.facebook.com/dialog/oauth" method="GET" class="loginFacebook">
		<input type="hidden" name="client_id" value="${facebok.app.id}"/>
		<input type="hidden" name="redirect_uri" value="${facebook.app.site_url}"/>
        <input type="hidden" name="scope" value="${facebook.app.permissions}"/>
		<input type="image" src="imagens/botao-login-facebook.png" alt="Entrar pelo Facebook">
	</form> --%>
	
	</body>
	
</html>