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
        } else if (response.status === 'not_authorized') {
            FB.logout();
            window.top.location = '/logout';
        } else {
            FB.logout();
            window.top.location = '/logout';
        }
    }
</script>

<%@ include file="/include/fb_auth.jsp" %>

<%@ include file="/include/header.jsp" %>

<p class="mensagemGenerica mensagemErro">Ocorreu um erro!</p>

<hr style="width: 500px"/>
<footer>
    <%@ include file="/include/footerInfo.jsp" %>
</footer>

</body>
</html>