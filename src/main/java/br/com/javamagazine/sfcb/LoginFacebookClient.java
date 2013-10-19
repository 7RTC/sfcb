package br.com.javamagazine.sfcb;

import java.io.IOException;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.WebRequestor;

public class LoginFacebookClient extends DefaultFacebookClient implements FacebookClient {

	static final String appId = "";
	static final String appSecret = "";
	
	public LoginFacebookClient (String accessToken) {
		super(accessToken);
	}
	
	public LoginFacebookClient () {}
	
	public FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {

	    WebRequestor wr = new DefaultWebRequestor();
	    WebRequestor.Response accessTokenResponse = wr.executeGet(
	            "https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl
	            + "&client_secret=" + appSecret + "&code=" + code);

	    return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
	}
}

