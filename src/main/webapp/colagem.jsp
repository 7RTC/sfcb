<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>

<html>
<head>
<title>SFCB - Colagens</title>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="js/jcollage.js"></script>
<script type="text/javascript" src="js/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="stylesheets/skins/tango/skin.css" />
<link rel="stylesheet" type="text/css" href="stylesheets/style.css">

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
		<div class="pushLeft">
			<img src="" id="imagemUsuario" class="imagemUsuario" />
			<span id="nomeUsuario" class="nomeUsuario"></span> 
			<span id="botaoFb" class="botaoFb">
				<fb:login-button show-faces="false" autologoutlink="true"></fb:login-button>
			</span>
		</div>
	</div>

	<h1>Colagem</h1>
	<canvas id="collage" width="720" height="480"></canvas>
	<div class="about">
		Motor de colagem <a	href="http://radikalfx.com/2009/10/16/canvas-collage/">jCollage</a> 
		</br>
		<a href="http://sorgalla.com/jcarousel/">jCarousel</a> 
		</br> 
		<img src="https://developers.google.com/appengine/images/appengine-noborder-120x30.gif" alt="Powered by Google App Engine" />

	</div>
	<aside class="camadas">
		<h2>Camadas</h2>
		<ul>
			<li class="background"><img src="imagens/background.png">
				<h3>Background</h3>
				<div class="visible"></div></li>
		</ul>
		<div class="options">
			<span>Blending:</span> <select name="blending">
				<option value="source-over">Normal</option>
				<option value="xor">XOR</option>
				<option value="copy">Copy</option>
				<option value="lighter">Lighter</option>
			</select> <span>Opacidade:</span> <select name="opacity">
				<option value="1" selected="selected">100%</option>
				<option value="0.9">90%</option>
				<option value="0.8">80%</option>
				<option value="0.7">70%</option>
				<option value="0.6">60%</option>
				<option value="0.5">50%</option>
				<option value="0.4">40%</option>
				<option value="0.3">30%</option>
				<option value="0.2">20%</option>
				<option value="0.1">10%</option>
			</select> <span>Sombra:</span> <select name="shadow">
				<option value="true">Ativada</option>
				<option value="false">Desativada</option>
			</select>
		</div>
		<div class="buttons">
			<ul>
				<li class="remove"></li>
				<li class="up"></li>
				<li class="down"></li>
			</ul>
		</div>
	</aside>
	
	<a href="#" id="gerarColagem" class="botaoGenerico botaoPostar">POSTAR</a>
	
	<footer class="fotos" id="loading_footer" style="margin: 0 auto;">
		<h3>Carregando fotos</h3>
	</footer>
	<footer class="fotos" id="sfcb_footer" style="display: none">
		<input type="hidden" value="${sessionScope.accessToken}"
			id="access-token" />
		<form id="formColagem" action="/postColagem" method="POST">
			<input type="text" id="dataColagem" name="dataColagem">
		</form>

		<ul id="mycarousel" class="jcarousel-skin-tango">
			<!-- The content goes in here -->
		</ul>
	</footer>


</body>
</html>