package com.example.galleryfilter.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.example.galleryfilter.twitter.TwitterAuthClient;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserV2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@SuppressWarnings("deprecation")
@RequestMapping("auth")
public class AuthController {
    @Autowired
    TwitterAuthClient twitterAuthClient;

    @Autowired HttpSession session;

    @GetMapping
    public ResponseEntity<?> login() {
        var redirectUrl = twitterAuthClient.requestReqToken();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, redirectUrl);

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping(params = "oauth_verifier")
    public ResponseEntity<?> callback(@RequestParam("oauth_token") String oauthToken, @RequestParam("oauth_verifier") String verifier){
        var twitterClient = twitterAuthClient.requestAccessToken(oauthToken, verifier);
        UserV2 user = (UserV2)twitterClient.getUserFromUserId(twitterClient.getUserIdFromAccessToken());
        return ResponseEntity.ok().body(user.getData());
    }
    
}
