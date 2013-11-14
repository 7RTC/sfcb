<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:og="http://ogp.me/ns#"
      xmlns:fb="https://www.facebook.com/2008/fbml">

<head>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="stylesheets/style.css">
    <title>SFCB - Demo</title>
    <meta property="og:image" content="http://jm-sfcb.appspot.com/imagens/bannerlike.png"/>
	<meta property="og:image:secure_url" content="https://jm-sfcb.appspot.com/imagens/bannerlike.png" />
    
    <script>
		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
		ga('create', 'UA-19890822-3', 'jm-sfcb.appspot.com');
		ga('send', 'pageview');
	</script>
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
                        $("#carregando").show();
                        window.top.location = '${facebook.app.site_url}' + "?accessToken=" + accessToken + "&expiresIn=" + expiresIn + "&userID=" + userID;
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
    <div id="carregando" style="display: none; margin-bottom: 20px;">
        <h2>Carregando...</h2>
        <br/>
        <img src="${req.contextPath}/imagens/ajax-loader.gif"/>
    </div>
    <fb:login-button show-faces="true" max-rows="1" size="xlarge" scope="${facebook.app.permissions}">Logar com
        Facebook</fb:login-button>
</div>

<%--  	<form action="https://www.facebook.com/dialog/oauth" method="GET" class="loginFacebook">
		<input type="hidden" name="client_id" value="${facebok.app.id}"/>
		<input type="hidden" name="redirect_uri" value="${facebook.app.site_url}"/>
        <input type="hidden" name="scope" value="${facebook.app.permissions}"/>
		<input type="image" src="imagens/botao-login-facebook.png" alt="Entrar pelo Facebook">
	</form> --%>

</body>

</html>