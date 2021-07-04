package com.example.galleryfilter.config;


import com.example.galleryfilter.twitter.TwitterAuthClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class TwitterConfig {
    
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
}
