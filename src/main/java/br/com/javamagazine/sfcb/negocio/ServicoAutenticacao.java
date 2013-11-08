package br.com.javamagazine.sfcb.negocio;

import br.com.javamagazine.sfcb.modelo.Token;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.WebRequestor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicoAutenticacao extends ServicoFacebook {
    private static final Logger log = Logger.getLogger(ServicoAutenticacao.class.getName());

    private final String appId;
    private final String appSecret;
    private final String redirectUrl;
    private final String scope;
    private final String code;
    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    private final MemcacheService cache = MemcacheServiceFactory.getMemcacheService("tokens");

    public ServicoAutenticacao() {
        this(null);
    }
    
    // Code é apenas necessário para login sem a utilização do Javascript SDK
    // Utilizado o Javascript SDK é possível obter o access token direto da resposta
    public ServicoAutenticacao(String code) {
        final Properties p = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/facebook-app.properties")) {
            p.load(inputStream);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Não foi possível abrir o arquivo facebook-app-id.properties", e);
        }

        appId = p.getProperty("facebok.app.id");
        appSecret = p.getProperty("facebook.app.secret");
        scope = p.getProperty("facebook.app.permissions");
        redirectUrl = p.getProperty("facebook.app.site_url");
        this.code = code;
        cache.setErrorHandler(ErrorHandlers.getStrict());

    }

    public FacebookClient.AccessToken getFacebookUserToken() throws IOException {

        final WebRequestor wr = new DefaultWebRequestor();
        final String urlCall = String.format("https://graph.facebook.com/oauth/access_token" +
                "?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s&scope=%s",
                appId, redirectUrl, appSecret, code, scope
        );
        log.info("URL called: " + urlCall);
        WebRequestor.Response accessTokenResponse = wr.executeGet(urlCall);
        return FacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
    }

    public FacebookClient.AccessToken extendUserToken(String shortLivedToken) throws IOException {

        final WebRequestor wr = new DefaultWebRequestor();
        final String urlCall = String.format("https://graph.facebook.com/oauth/access_token?" +
                "grant_type=fb_exchange_token&client_id=%s&client_secret=%s&fb_exchange_token=%s",
                appId, appSecret, shortLivedToken
        );
        log.info("URL called: " + urlCall);
        WebRequestor.Response accessTokenResponse = wr.executeGet(urlCall);
        return FacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
    }
    
    public FacebookClient.AccessToken getFacebookUserToken(String accessToken, String tempoExpiracao) throws IOException {
    	
    	return FacebookClient.AccessToken.fromQueryString(
				"access_token=" + accessToken 
				+ "&expires=" + tempoExpiracao);
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public Token createAccessToken(FacebookClient.AccessToken accessToken, String userID) {
        final Entity token = new Entity("Token", userID);

        token.setProperty("accessToken", accessToken.getAccessToken());
        token.setProperty("expireDate", accessToken.getExpires());
        token.setProperty("UUID", generateUUID());

        datastore.put(token);

        return Token.fromEntity(token);
    }

    public Token storeInMemcache(FacebookClient.AccessToken accessToken, String userID) {
       String uuid;
        if (cache.contains(userID + "_uuid")) {
            uuid = (String) cache.get(userID + "_uuid");
        } else {
            uuid = generateUUID();
            cache.put(userID + "_uuid", uuid, Expiration.onDate(accessToken.getExpires()));
        }

        log.info("Token UUID: " + uuid);

        final Token token = Token.fromFBToken(accessToken, userID, uuid);
        cache.put(uuid, token, Expiration.onDate(accessToken.getExpires()));

        return token;

    }

    public Token getFromMemcache(String uuid) {
        return (Token) cache.get(uuid);
    }

    public String getAccessToken(String userID) throws EntityNotFoundException {
        final Key k = KeyFactory.createKey("Token", userID);

        return  Token.fromEntity(datastore.get(k)).getAccessToken();
    }

    public Token updateExpirationDate(Token t, Date expirationDate) {
        t.setExpiracao(expirationDate);
        datastore.put(Token.toEntity(t));

        return t;
    }

    public Token getSFCBAccessToken(String accessToken) {
        final Query.Filter accTokenfilter =
                new Query.FilterPredicate("accessToken",
                        Query.FilterOperator.EQUAL,
                        accessToken);

        final Query q = new Query("Token").setFilter(accTokenfilter);
        final PreparedQuery pq = datastore.prepare(q);
        final Entity entity = pq.asSingleEntity();

        final Token t;
        if (entity != null) {
            t = Token.fromEntity(entity);
        } else {
            t = null;
        }

        return t;
    }
}

