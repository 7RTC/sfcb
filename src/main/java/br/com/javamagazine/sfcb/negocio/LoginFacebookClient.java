package br.com.javamagazine.sfcb.negocio;

import java.io.IOException;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.WebRequestor;

public class LoginFacebookClient extends DefaultFacebookClient implements FacebookClient {

    static final String appId = "";
    static final String appSecret = "";
    static final String scope = "user_photos,friends_photos,publish_stream,photo_upload";

    public LoginFacebookClient(String accessToken) {
        super(accessToken);
    }

    public LoginFacebookClient() {
    }

    public FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {

        final WebRequestor wr = new DefaultWebRequestor();
        final String urlCall = String.format("https://graph.facebook.com/oauth/access_token" +
                "?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s&scope=%s",
                appId, redirectUrl, appSecret, code, scope
        );
        WebRequestor.Response accessTokenResponse = wr.executeGet(urlCall);

        return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
    }
}

