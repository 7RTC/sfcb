<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="fb-root"></div>
<script>
    var myDomain = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');

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

    window.fbAsyncInit = function () {
        FB.init({
            appId: '${facebok.app.id}',
            channelUrl: myDomain + '/channel.html', // Channel File
            status: true,
            cookie: false,
            xfbml: true
        });

        FB.Event.subscribe('auth.authResponseChange', authResponseChangeCallback);

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
</script>