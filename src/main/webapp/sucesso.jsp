<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:og="http://ogp.me/ns#"
      xmlns:fb="https://www.facebook.com/2008/fbml">
<head>
    <title>SFCB - Colagens</title>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="stylesheets/style.css">
    <%@ include file="include/fognmeta.jsp" %>

    <script type="text/javascript">
        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-19890822-4']);
        _gaq.push(['_trackPageview']);
        (function () {
            var ga = document.createElement('script');
            ga.type = 'text/javascript';
            ga.async = true;

            ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';

            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(ga, s);
        })();
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

        FB.Event.subscribe('auth.authResponseChange', function (response) {
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
        });
        FB.Event.subscribe("auth.logout", function () {
            window.location = '/logout';
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

    function setaDadosUsuario() {
        FB.api('/me', function (response) {
            $("#nomeUsuario").text(response.name);
            $("#imagemUsuario").attr(
                    "src",
                    "https://graph.facebook.com/" + response.id
                            + "/picture?width=25&height=25");
            // Para o IE
            $(".pushRight").css("display", "inline");
        });
    }

    function carregaColagem() {
        FB.api('/${param.idImagem}?fields=source', function (response) {
            $("#imagemColagem").attr("src", response.source);
        });
    }
</script>

<%@ include file="include/header.jsp" %>

<p class="mensagemGenerica mensagemSucesso">Colagem enviada com sucesso!</p>

<img id="imagemColagem" alt="colagem" class="colagemSucesso"/>

<a href="/colagem" id="novaColagem" class="botaoGenerico">GERAR NOVA COLAGEM</a>
<a href="https://www.facebook.com/${param.idUsuario}/posts/${param.idPost}" target="blank" id="colagemFacebook"
   class="botaoGenerico">VER COLAGEM NO FACEBOOK</a>
<br/><br/>
<hr style="width: 700px"/>
<footer>
    <%@ include file="include/footerInfo.jsp" %>
</footer>

</body>
</html>