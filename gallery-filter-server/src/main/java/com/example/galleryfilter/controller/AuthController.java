package com.example.galleryfilter.controller;


import javax.servlet.http.HttpSession;

import com.example.galleryfilter.twitter.TwitterAuthClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.redouane59.twitter.dto.user.UserV2;

/**
 * TwitterでのOAuth認証を担当するクラス。
 * 実際のOAuthの処理は {@link TwitterAuthClient}で行う。
 */
@Controller
@RequestMapping("auth")
public class AuthController {
    @Autowired
    TwitterAuthClient twitterAuthClient;

    @Autowired
    HttpSession session;

    /**
     * OAuth認証用のTwitterのURLを返却するメソッド。
     * 
     * @return Twitterの認証画面へリダイレクトする303レスポンス。
     */
    @GetMapping
    public ResponseEntity<?> login() {
        var redirectUrl = twitterAuthClient.requestReqToken();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, redirectUrl);

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    /**
     * ツイッターで認証後、ツイッターからリダイレクトされるメソッド。
     * 
     * @param oauthToken loginメソッドで送ったのと同じリクエストトークン。
     * @param verifier アクセストークンをTwitterに要求するための認証文字列。
     * @return フロントのページ(Vueのページ)へリダイレクトする303レスポンス。
     */
    @GetMapping(params = "oauth_verifier")
    public ResponseEntity<?> callback(@RequestParam("oauth_token") String oauthToken, @RequestParam("oauth_verifier") String verifier){
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/");
        twitterAuthClient.requestAccessToken(oauthToken, verifier);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
    
    /**
     * 現在のアクセストークンと紐づくユーザ情報を返却するメソッド。
     * 
     * @return 現在認証中のユーザ情報。
     */
    @GetMapping(value="user")
    public ResponseEntity<?> getProfile() {
        var twitterClient = twitterAuthClient.getTwitterClient();
        if (twitterClient == null){
            return ResponseEntity.badRequest().build();
        }
        UserV2 user = (UserV2)twitterClient.getUserFromUserId(twitterClient.getUserIdFromAccessToken());
        return ResponseEntity.ok().body(user.getData());
    }
    
}
