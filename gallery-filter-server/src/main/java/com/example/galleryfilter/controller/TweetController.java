package com.example.galleryfilter.controller;

import java.util.List;

import com.example.galleryfilter.twitter.TwitterAuthClient;

import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tweet")
public class TweetController {

    @Autowired
    TwitterAuthClient twitterAuthClient;

    @GetMapping("/{name}")
    public List<TweetData> getUsersTweet(@PathVariable("name") String userName) {
        var twitterClient = twitterAuthClient.getTwitterClient();
        var list = twitterClient.getUserTimeline(twitterClient.getUserFromUserName(userName).getId());
        return list.getData();
    }
    
}
