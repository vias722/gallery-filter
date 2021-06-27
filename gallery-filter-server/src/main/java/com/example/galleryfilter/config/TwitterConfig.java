package com.example.galleryfilter.config;


import com.example.galleryfilter.twitter.TwitterAuthClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class TwitterConfig {
    private static String twitterAuthUrl = "https://twitter.com/oauth";
    private static String requestTokenUrl = twitterAuthUrl + "/request_token";
    private static String authenticateUrl = twitterAuthUrl + "/authorize?oauth_token=";
    private static String accessTokenUrl = twitterAuthUrl + "/access_token";
    
    @Bean
    @Primary
    @SessionScope
    TwitterAuthClient twitterAuthClient(){
        return new TwitterAuthClient();
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    // @Bean
    // @SuppressWarnings("deprecation")
    // OAuthRestTemplate oAuthRestTemplate(@Value("${twitter.apikey}")String apiKey, 
    //     @Value("${twitter.apisecret}") String apiSecret){
    //     var details = new BaseProtectedResourceDetails();
    //     details.setId("twitter");
    //     details.setConsumerKey(apiKey);
    //     details.setSharedSecret(new SharedConsumerSecretImpl(apiSecret));
    //     details.setAccessTokenURL(accessTokenUrl);
    //     details.setRequestTokenURL(requestTokenUrl);
    //     details.setUserAuthorizationURL(authenticateUrl);
    //     return new OAuthRestTemplate(details);
    // }
}
