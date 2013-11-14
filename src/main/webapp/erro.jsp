<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>

<html xmlns:fb="http://ogp.me/ns/fb#">
<head>
	<title>SFCB - Colagens</title>
	<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
	<meta property="og:image" content="http://jm-sfcb.appspot.com/imagens/bannerlike.png"/>
	<meta property="og:image:secure_url" content="https://jm-sfcb.appspot.com/imagens/bannerlike.png" />
</head>
<body>

	<div id="fb-root"></div>
	<script>
		var myDomain = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
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
			FB.api('/me', function(response) {
				$("#nomeUsuario").text(response.name);
				$("#imagemUsuario").attr(
						"src",
						"https://graph.facebook.com/" + response.id
								+ "/picture?width=25&height=25");
			});
		}
	</script>

	<div class="loginFacebookColagem">
		<div class="pushRight">
			<img src="" id="imagemUsuario" class="imagemUsuario" />
			<span id="nomeUsuario" class="nomeUsuario"></span> 
			<span id="botaoFb" class="botaoFb">
				<fb:login-button show-faces="false" autologoutlink="true"></fb:login-button>
			</span>
		</div>
		<div class="pushLeft">
			<fb:like href="https://jm-sfcb.appspot.com/" layout="button_count" action="like" show_faces="false" share="true"></fb:like>
		</div>
		<h1 class="tituloLogado">Colagem</h1>
	</div>

	<p class="mensagemGenerica mensagemErro">Ocorreu um erro!</p>

</body>
</html>