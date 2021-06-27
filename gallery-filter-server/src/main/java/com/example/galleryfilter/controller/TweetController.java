package com.example.galleryfilter.controller;

import java.util.List;

import com.example.galleryfilter.twitter.TwitterAuthClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tweet")
public class TweetController {

    @Autowired
    TwitterAuthClient twitterAuthClient;

    @Autowired
    ObjectMapper mapper;

    @GetMapping("/{name}")
    public ResponseEntity<?> getUsersTweet(@PathVariable("name") String userName) {
        // var twitterClient = twitterAuthClient.getTwitterClient();
        // var list = twitterClient.getUserTimeline(twitterClient.getUserFromUserName(userName).getId());
        var request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/home_timeline.json");
        var response = twitterAuthClient.executeRequest(request);
        try {
            return new ResponseEntity<>(mapper.readTree(response.getBody()), HttpStatus.OK) ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
