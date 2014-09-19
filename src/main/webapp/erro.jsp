<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:og="http://ogp.me/ns#"
      xmlns:fb="https://www.facebook.com/2008/fbml">
<head>
    <title>SFCB - Colagens</title>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="stylesheets/style.css">
    <%@ include file="/include/fognmeta.jsp" %>
</head>
<body>

<script>
    function authResponseChangeCallback(response) {
        if (response.status === 'connected') {
            setaDadosUsuario();
        } else if (response.status === 'not_authorized') {
            window.top.location = '/logout';
        } else {
            window.top.location = '/logout';
        }
    }
</script>

<%@ include file="/include/fb_auth.jsp" %>

<%@ include file="/include/header.jsp" %>

<p class="mensagemGenerica mensagemErro">Ocorreu um erro!</p>

<p class="erroExplicativo">Ocorreu um erro inesperado ao executar a ação requisitada. Possivelmente estamos lidando com
    uma indisponibilidade temporária. Por favor tente novamente em alguns minutos.</p>

<p class="erroExplicativo">Caso o erro persista por favor abra um ticket em nosso <a
        href="https://bitbucket.org/7RTC/sfcb">sistema de ocorrências</a> relatando o ocorrido e verificaremos o
    problema assim que possível.</p>

<a href="/" id="novaColagem" class="botaoGenerico botaoNovaColagemErro">Gerar nova colagem</a>

<footer class="footerInfo">
    <%@ include file="/include/footerInfo.jsp" %>
</footer>

</body>
</html>