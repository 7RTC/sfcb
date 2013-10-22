<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html><head>
		<title>Colagens</title>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script type="text/javascript" src="js/jcollage.js"></script>
		<script type="text/javascript" src="js/script.js"></script>
		<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
	</head>
	<body>
		<h1>Colagens</h1>
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
				<span>Opacity:</span>
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
				<span>Shadow:</span>
				<select name="shadow">
					<option value="true">On</option>
					<option value="false">Off</option>
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
		<footer class="fotos">
			<form id="formColagem" action="/postColagem" method="POST">
				<input type="text" id="dataColagem" name="dataColagem">
			</form>
			
			<input type="button" value="Obter Fotos" id="recuperarFotos"/>
			<input type="button" value="Gerar Colagem" id="gerarColagem"/>
			<ul></ul>
		</footer>
		
	
</body></html>