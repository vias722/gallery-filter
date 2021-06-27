package com.example.galleryfilter.twitter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.others.RequestToken;
import io.github.redouane59.twitter.helpers.AbstractRequestHelper;
import io.github.redouane59.twitter.signature.TwitterCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class TwitterAuthClient {
    private static String twitterAuthUrl = "https://twitter.com/oauth";
    private static String requestTokenUrl = twitterAuthUrl + "/request_token";
    private static String authenticateUrl = twitterAuthUrl + "/authenticate?oauth_token=";
    private static String accessTokenUrl = twitterAuthUrl + "/access_token";

    @Value("${twitter.apikey}")
    private String apiKey;
    @Value("${twitter.apisecret}")
    private String apiSecret;

    private String oauthToken;
    private RequestToken requestToken;

    @Value("${twitter.accesstoken}")
    private String accessToken;
    @Value("${twitter.accesstokensecret}")
    private String accessTokenSecret;

    private TwitterClient twitterClient;

    private TwitterClient commonTwitterClient;

    @Autowired
    RestTemplate restTemplate;

    @PostConstruct
    public void init(){
        TwitterCredentials cred = new TwitterCredentials();
        cred.setApiKey(apiKey);
        cred.setApiSecretKey(apiSecret);
        cred.setAccessToken(accessToken);
        cred.setAccessTokenSecret(accessTokenSecret);

        commonTwitterClient = new TwitterClient(cred);
    }

    public String requestReqToken() {
        // Map<String, String> map = new HashMap<>();
        // map.put("oauth_callback", "http://localhost:8080/auth");

        // var response = restTemplate.postForObject(requestTokenUrl, map, JsonNode.class);
        // oauthToken = response.get("oauth_token").asText();

        // return authenticateUrl + oauthToken;
        var token = commonTwitterClient.getOauth1Token("http://localhost:8080/api/v1/auth");
        oauthToken = token.getOauthToken();
        requestToken = token;
        return authenticateUrl + oauthToken;
    }

    public TwitterClient requestAccessToken(String oauthToken, String verifier) {
        Map<String, String> map = new HashMap<>();
        // map.put("oauth_consumer_key", apiKey);
        // map.put("oauth_token", oauthToken);
        // map.put("oauth_verifier", verifier);

        // var response = restTemplate.postForObject(accessTokenUrl, map, JsonNode.class);
        // accessToken = response.get("oauth_token").asText();
        // accessTokenSecret = response.get("oauth_token_secret").asText();
        //requestToken.setOauthToken(oauthToken);
        // var accessToken = commonTwitterClient.getOAuth1AccessToken(requestToken, verifier);

        TwitterCredentials precred = new TwitterCredentials();
        precred.setApiKey(apiKey);
        precred.setApiSecretKey(apiSecret);
        precred.setAccessToken(requestToken.getOauthToken());
        precred.setAccessTokenSecret(requestToken.getOauthTokenSecret());

        var pretwitterClient = new TwitterClient(precred);

        var accessToken = pretwitterClient.getOAuth1AccessToken(requestToken, verifier);

        this.accessToken = accessToken.getOauthToken();
        this.accessTokenSecret = accessToken.getOauthTokenSecret();

        TwitterCredentials cred = new TwitterCredentials();
        cred.setApiKey(apiKey);
        cred.setApiSecretKey(apiSecret);
        cred.setAccessToken(this.accessToken);
        cred.setAccessTokenSecret(accessTokenSecret);

        twitterClient = new TwitterClient(cred);

        return twitterClient;
    }

    public TwitterClient getTwitterClient(){
        return twitterClient;
    }
}
