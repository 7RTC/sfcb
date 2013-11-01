package br.com.javamagazine.sfcb.negocio;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

public abstract class ServicoFacebook {

    final protected String accessToken;
    final protected FacebookClient client;

    public ServicoFacebook() {
        this(null);
    }

    public ServicoFacebook(String accessToken) {
        this(accessToken, new DefaultFacebookClient(accessToken));
    }

    public ServicoFacebook(String accessToken, FacebookClient client) {
        this.accessToken = accessToken;
        this.client = client;
    }

    public FacebookClient getClient() {
        return client;
    }
}
