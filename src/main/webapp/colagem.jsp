<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html><head>
		<title>Colagens</title>
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
		  // Additional JS functions here

			window.fbAsyncInit = function() {
				FB.init({
					appId : '${facebok.app.id}', // App ID
					status : true, // check login status
					cookie : true, // enable cookies to allow the server to access the session
					xfbml : true
				// parse XFBML
				});

				FB.Event.subscribe('auth.authResponseChange',
						function(response) {
							// Here we specify what we do with the response anytime this event occurs. 
							if (response.status === 'connected') {
								// The response object is returned with a status field that lets the app know the current
								// login status of the person. In this case, we're handling the situation where they 
								// have logged in to the app.
								testAPI();
								var accessToken = response.authResponse.accessToken;
								var expiresIn = response.authResponse.expiresIn;
							} else if (response.status === 'not_authorized') {
								// In this case, the person is logged into Facebook, but not into the app, so we call
								// FB.login() to prompt them to do so. 
								// In real-life usage, you wouldn't want to immediately prompt someone to login 
								// like this, for two reasons:
								// (1) JavaScript created popup windows are blocked by most browsers unless they 
								// result from direct interaction from people using the app (such as a mouse click)
								// (2) it is a bad experience to be continually prompted to login upon page load.
								FB.login();
							} else {
								// In this case, the person is not logged into Facebook, so we call the login() 
								// function to prompt them to do so. Note that at this stage there is no indication
								// of whether they are logged into the app. If they aren't then they'll see the Login
								// dialog right after they log in to Facebook. 
								// The same caveats as above apply to the FB.login() call here.
								FB.login();
							}
						});
			};

			// Load the SDK asynchronously
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
			
			  function testAPI() {
				    console.log('Welcome!  Fetching your information.... ');
				    FB.api('/me', function(response) {
				      console.log('Good to see you, ' + response.name + '.');
				      $("#nomeUsuario").text(response.name);
				      
				      $("#imagemUsuario").attr("src", "https://graph.facebook.com/"+response.id+"/picture?width=25&height=25");
				    });
				  }
		</script>
				
		<div class="loginFacebookColagem"> 
		</div>
				
		<h1>Colagens <img src="" id="imagemUsuario"/> <span id="nomeUsuario"></span> <fb:login-button show-faces="false" autologoutlink="true"></fb:login-button>
		</h1>
		<canvas id="collage" width="720" height="480"></canvas>
		<div class="about">
			Demonstração de colagem retirada do site: <a href="http://radikalfx.com/2009/10/16/canvas-collage/">radikalFX</a>
		</div>
		<aside class="camadas">
			<h2>Camadas</h2>
			<ul>
				<li class="background">
					<img src="imagens/background.png">
					<h3>Background</h3>
					<div class="visible"></div>
				</li>
			</ul>
			<div class="options">
				<span>Blending:</span>
				<select name="blending">
					<option value="source-over">Normal</option>
					<option value="xor">XOR</option>
					<option value="copy">Copy</option>
					<option value="lighter">Lighter</option>
				</select>
				<span>Opacidade:</span>
				<select name="opacity">
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
				</select>
				<span>Sombra:</span>
				<select name="shadow">
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
        <footer class="fotos" id="loading_footer" style="margin: 0 auto;">
            <h3>Carregando fotos</h3>
        </footer>
		<footer class="fotos" id="sfcb_footer" style="display: none">
            <input type="hidden" value="${sessionScope.accessToken}" id="access-token" />
			<form id="formColagem" action="/postColagem" method="POST">
				<input type="text" id="dataColagem" name="dataColagem">
			</form>
			
			<input type="button" value="Obter Fotos" id="recuperarFotos"/>
			<input type="button" value="Gerar Colagem" id="gerarColagem"/>
			<input type="button" value="Página Anterior" id="paginaAnterior" style="display: none"/>
			<input type="button" value="Próxima Página" id="proximaPagina" style="display: none"/>
			<ul id="mycarousel" class="jcarousel-skin-tango">
   				<!-- The content goes in here -->
			</ul>
		</footer>
		
	
</body></html>