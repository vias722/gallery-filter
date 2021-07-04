package com.example.galleryfilter.controller;

import com.example.galleryfilter.service.PhotoFilteringService;
import com.example.galleryfilter.twitter.TwitterAuthClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 主にタイムライン/リストからツイートの取得を行うクラス。
 * ここから返却されるツイートは、すべてイラスト/写真の分類済みであり、
 * 文章のみのツイートは含まれていない。
 */
@RestController
@RequestMapping("tweet")
public class TweetController {

    private static String tweetCount = "200";

    @Autowired
    TwitterAuthClient twitterAuthClient;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    PhotoFilteringService photoFilteringService;

    @GetMapping
    public ResponseEntity<?> getUsersTweet() {
        var request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/home_timeline.json");
        request.addQuerystringParameter("count", tweetCount);
        var response = twitterAuthClient.executeRequest(request);

        try {
            var tweets = (ArrayNode)mapper.readTree(response.getBody());
            
            return new ResponseEntity<>(photoFilteringService.filteringTweets(tweets), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value="/list")
    public ResponseEntity<?> getLists() {
        var request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/lists/list.json");
        request.addQuerystringParameter("count", tweetCount);
        var response = twitterAuthClient.executeRequest(request);
        try {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value="/list/{id}")
    public ResponseEntity<?> getListTweets(@PathVariable String id) {
        var request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/lists/statuses.json");
        request.addQuerystringParameter("count", tweetCount);
        request.addQuerystringParameter("list_id", id);
        var response = twitterAuthClient.executeRequest(request);
        try {
            var tweets = (ArrayNode)mapper.readTree(response.getBody());
            
            return new ResponseEntity<>(photoFilteringService.filteringTweets(tweets), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
