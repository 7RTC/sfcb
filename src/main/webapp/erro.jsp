<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>

<html>
<head>
<title>SFCB - Colagens</title>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="stylesheets/style.css">

</head>
<body>

	<div id="fb-root"></div>
	<script>
		window.fbAsyncInit = function() {
			FB.init({
				appId : '${facebok.app.id}',
	            channelUrl: myDomain + '/channel.html', // Channel File
				status : true,
				cookie : false,
				xfbml : true
			});

			FB.Event.subscribe('auth.authResponseChange', function(response) {
				if (response.status === 'connected') {
					setaDadosUsuario();
				} else if (response.status === 'not_authorized') {
					FB.logout();
					window.top.location = '/logout';
				} else {
					FB.logout();
					window.top.location = '/logout';
				}
			});
			FB.Event.subscribe("auth.logout", function() {
				window.location = '/logout';
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
				$("#nomeUsuario").text(response.name);
				$("#imagemUsuario").attr(
						"src",
						"https://graph.facebook.com/" + response.id
								+ "/picture?width=25&height=25");
			});
		}
	</script>

	<div class="loginFacebookColagem">
		<div class="pushLeft">
			<img src="" id="imagemUsuario" class="imagemUsuario" />
			<span id="nomeUsuario" class="nomeUsuario"></span> 
			<span id="botaoFb" class="botaoFb">
				<fb:login-button show-faces="false" autologoutlink="true"></fb:login-button>
			</span>
		</div>
	</div>

	<h1>Colagem</h1>
	
	<p class="mensagemGenerica mensagemErro">Ocorreu um erro!</p>

</body>
</html>