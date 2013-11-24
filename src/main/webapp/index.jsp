<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:og="http://ogp.me/ns#"
      xmlns:fb="https://www.facebook.com/2008/fbml">

<head>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="stylesheets/style.css">
    <title>Social Facebook Collage Builder</title>
    <%@ include file="/include/fognmeta.jsp" %>
</head>

<body>

<div id="fb-root"></div>
<script>
    var myDomain = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
    window.fbAsyncInit = function () {
        FB.init({
            appId: '${facebok.app.id}',
            channelUrl: myDomain + '/channel.html', // Channel File
            status: true,
            cookie: false,
            xfbml: true
        });

        FB.Event.subscribe('auth.authResponseChange',
                function (response) {
                    if (response.status === 'connected') {
                        var accessToken = response.authResponse.accessToken;
                        var expiresIn = response.authResponse.expiresIn;
                        var userID = response.authResponse.userID;
                        $("#loginMain").hide();
                        $("#carregando").show();
                        window.top.location = '${facebook.app.site_url}' + "/login?accessToken=" + accessToken + "&expiresIn=" + expiresIn + "&userID=" + userID;
                    } else if (response.status === 'not_authorized') {
                        FB.login();
                    } else {
                        FB.login();
                    }
                });

    };

    // Carrega o SDK do Facebook de modo assíncrono
    (function (d) {
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

</script>

<h1>Aplicação demonstração de colagens</h1>

<div class="loginFacebook">
    <div id="loginMain">
        <img src="${pageContext.request.contextPath}/imagens/logoindex.jpg" alt="Social Facebook Collage Builder"/>
    </div>
    <div id="carregando" style="display: none">
        <h2>Carregando...</h2>
        <br/>
        <img src="${pageContext.request.contextPath}/imagens/ajax-loader.gif"/>
    </div>
    <fb:login-button show-faces="true" max-rows="1" size="xlarge" scope="${facebook.app.permissions}">Logar com
        Facebook</fb:login-button>
    <hr style="width: 500px"/>
    <footer>
        <%@ include file="/include/footerInfo.jsp" %>
    </footer>
</div>

<%--  	<form action="https://www.facebook.com/dialog/oauth" method="GET" class="loginFacebook">
		<input type="hidden" name="client_id" value="${facebok.app.id}"/>
		<input type="hidden" name="redirect_uri" value="${facebook.app.site_url}/login"/>
        <input type="hidden" name="scope" value="${facebook.app.permissions}"/>
		<input type="image" src="imagens/botao-login-facebook.png" alt="Entrar pelo Facebook">
	</form> --%>

</body>

</html>