<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:og="http://ogp.me/ns#"
      xmlns:fb="https://www.facebook.com/2008/fbml">
<head>
    <title>SFCB - Colagens</title>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="stylesheets/style.css">
    <%@ include file="/include/fognmeta.jsp" %>
</head>
<body>

<script>
    function authResponseChangeCallback(response) {
        if (response.status === 'connected') {
            setaDadosUsuario();
            carregaColagem();
        } else if (response.status === 'not_authorized') {
            FB.logout();
            window.top.location = '/logout';
        } else {
            FB.logout();
            window.top.location = '/logout';
        }
    }

    function carregaColagem() {
        FB.api('/${param.idImagem}?fields=source', function (response) {
            $("#imagemColagem").attr("src", response.source);
        });
    }
</script>

<%@ include file="/include/fb_auth.jsp" %>

<%@ include file="/include/header.jsp" %>

<p class="mensagemGenerica mensagemSucesso">Colagem enviada com sucesso!</p>

<img id="imagemColagem" alt="colagem" class="colagemSucesso"/>

<a href="/colagem" id="novaColagem" class="botaoGenerico">GERAR NOVA COLAGEM</a>
<a href="https://www.facebook.com/${param.idUsuario}/posts/${param.idPost}" target="blank" id="colagemFacebook"
   class="botaoGenerico">VER COLAGEM NO FACEBOOK</a>
<br/><br/>
<hr style="width: 700px"/>
<footer>
    <%@ include file="/include/footerInfo.jsp" %>
</footer>

</body>
</html>