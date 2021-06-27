package com.example.galleryfilter.twitter;

import javax.annotation.PostConstruct;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.springframework.beans.factory.annotation.Value;

import io.github.redouane59.twitter.signature.TwitterCredentials;

public class TwitterAuthClient {
    private static String twitterAuthUrl = "https://twitter.com/oauth";
    private static String requestTokenUrl = twitterAuthUrl + "/request_token";
    private static String authenticateUrl = twitterAuthUrl + "/authenticate?oauth_token=";
    private static String accessTokenUrl = twitterAuthUrl + "/access_token";

    @Value("${twitter.apikey}")
    private String apiKey;
    @Value("${twitter.apisecret}")
    private String apiSecret;

    private OAuth10aService service;
    private OAuth1RequestToken requestToken;

    private OAuth1AccessToken accessToken;

    private TwitterClient twitterClient;

    @PostConstruct
    public void init(){
        service = new ServiceBuilder(apiKey)
            .apiSecret(apiSecret)
            .callback("http://localhost:8080/api/v1/auth")
            .build(TwitterApi.instance());
    }

    public String requestReqToken() {
        try {
            var token = service.getRequestToken();
            return service.getAuthorizationUrl(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TwitterClient requestAccessToken(String oauthToken, String verifier) {
        try {
            accessToken = service.getAccessToken(requestToken, verifier);

            var cred = new TwitterCredentials(apiKey, apiSecret, accessToken.getToken(), accessToken.getTokenSecret());

            twitterClient = new TwitterClient(cred);
            return twitterClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }

    public TwitterClient getTwitterClient(){
        return twitterClient;
    }

    public Response executeRequest(OAuthRequest request){
        service.signRequest(accessToken, request);
        try {
            return service.execute(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
}
