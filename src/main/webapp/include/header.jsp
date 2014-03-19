<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<header class="loginFacebookColagem">
    <div class="pushRight">
        <img src="" id="imagemUsuario" class="imagemUsuario"/>
        <span id="nomeUsuario" class="nomeUsuario"></span>
			<span id="botaoFb" class="botaoFb">
				<fb:login-button show-faces="false" autologoutlink="true" scope="${facebook.app.permissions}"></fb:login-button>
			</span>
    </div>
    <div class="pushLeft">
        <fb:like href="${facebook.app.site_url}" layout="button_count" action="like" show_faces="false" share="true"></fb:like>
    </div>
    <h1 class="tituloLogado"><a href="/">Colagem</a></h1>
</header>