<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:og="http://ogp.me/ns#"
      xmlns:fb="https://www.facebook.com/2008/fbml">
<head>
    <title>SFCB - Colagens</title>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script type="text/javascript" src="js/jcollage.js"></script>
    <script type="text/javascript" src="js/jquery.jcarousel.min.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <link rel="stylesheet" type="text/css" href="stylesheets/skins/tango/skin.css"/>
    <link rel="stylesheet" type="text/css" href="stylesheets/style.css">
    <%@ include file="/include/fognmeta.jsp" %>
</head>
<body class="unselectable">

<script>
    function authResponseChangeCallback(response) {
        if (response.status === 'connected') {
            setaDadosUsuario();
            recuperaAlbuns();
        } else if (response.status === 'not_authorized') {
            FB.logout();
            window.top.location = '/logout';
        } else {
            FB.logout();
            window.top.location = '/logout';
        }
    }

    function recuperaAlbuns() {
        var query = "SELECT object_id, name, photo_count FROM album WHERE owner = me() AND photo_count > 0";
        var queryEncoded = encodeURIComponent(query);

        FB.api("/fql?q=" + queryEncoded , function(response) {
            var arrayData = response.data;
            for (var i = 0; i < arrayData.length; i++) {
                $("#comboAlbuns").append($("<option></option>")
                        .attr("value", arrayData[i].object_id)
                        .text(arrayData[i].name).data("photo-count", arrayData[i].photo_count));
            }
        });
    }
</script>

<%@ include file="/include/fb_auth.jsp" %>

<%@ include file="/include/header.jsp" %>

<section id="conteudo" class="conteudo">

    <canvas id="collage" width="720" height="480"></canvas>
    <div class="about">
        Motor de colagem <a href="http://radikalfx.com/2009/10/16/canvas-collage/">jCollage</a>
        <br/>
        <a href="http://sorgalla.com/jcarousel/">jCarousel</a>
        <br/>
        <img src="https://developers.google.com/appengine/images/appengine-noborder-120x30.gif"
             alt="Powered by Google App Engine"/>
        <hr />
        <%@ include file="/include/footerInfo.jsp" %>

    </div>
    <aside class="camadas">
        <h2>Camadas</h2>
	        <ul>
	            <li class="background"><img src="imagens/background.png">
	                <h3>Background</h3>
	                <div class="visible"></div>
	            </li>
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

</section>

<a href="#" id="gerarColagem" class="botaoGenerico botaoPostar">POSTAR</a>

<label for="comboAlbuns" class="labelComboAlbuns">Escolha um álbum do Facebook:</label>
<select id="comboAlbuns" name="comboAlbuns" class="comboGenerico comboAlbuns" disabled="disabled">
    <option value="0">Todos</option>
</select>

<input type="hidden" id="albumId" value="0"/>

<footer class="fotos">
	<div id="loading_footer" style="margin: 0 auto;">
	    <h3><img src="${pageContext.request.contextPath}/imagens/ajax-loader.gif"/> Carregando fotos</h3>
	</div>
	
	<div id="sfcb_footer" style="display: none">
	    <input type="hidden" value="${sessionScope.tokenUUID}" id="token-uuid"/>

	    <form id="formSucesso" action="${pageContext.request.contextPath}/sucesso" method="POST">
	        <input type="text" id="idUsuario" name="idUsuario">
	        <input type="text" id="idPost" name="idPost">
	        <input type="text" id="idImagem" name="idImagem">
	    </form>
	
	    <ul id="mycarousel" class="jcarousel-skin-tango">
	    </ul>
	</div>
</footer>

</body>
</html>