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

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
/**
 * TwitterとのOAuth認証を行うクラス。
 * 当初はtwitteredライブラリの{@link TwitterClient}で行う予定だったが、
 * 同ライブラリのユーザ認証の対応がイマイチだったため、OAuthライブラリであるscribejavaに変更。
 * 
 * プロパティ値として
 * twitter.apikey, twitter.apisecret それぞれを設定する必要がある。
 * (セキュリティ上の問題があるため、開発中は各開発環境の環境変数に設定し、デプロイ時はコンテナの環境変数に後付けで設定する想定)
 * 
 * Twitterの開発者ポータルからはAccessToken, AccessTokenSecretが取得可能であるため、
 * それらをプロパティ値として設定しておけば、開発環境ではアプリ起動ごとの認証をスキップできる。
 * 
 * TODO: 現状、エラーハンドリングは例外を垂れ流すに任せているため、要実装。
 */
public class TwitterAuthClient {

    @Value("${twitter.apikey}")
    private String apiKey;
    @Value("${twitter.apisecret}")
    private String apiSecret;

    @Value("${twitter.accesstoken:}")
    private String accessTokenString;
    @Value("${twitter.accesstokensecret:}")
    private String accessTokenSecretString;

    @Value("${app.url:http://localhost:8080}")
    private String appUrl;

    private OAuth10aService service;
    private OAuth1RequestToken requestToken;

    private OAuth1AccessToken accessToken;

    private TwitterClient twitterClient;

    @PostConstruct
    public void init(){
        service = new ServiceBuilder(apiKey)
            .apiSecret(apiSecret)
            .callback(appUrl+"/auth")
            .build(TwitterApi.instance());
        if (accessTokenString != null && accessTokenSecretString != null){
            accessToken = new OAuth1AccessToken(accessTokenString, accessTokenSecretString);
            var cred = new TwitterCredentials(apiKey, apiSecret, accessToken.getToken(), accessToken.getTokenSecret());

            twitterClient = new TwitterClient(cred);
        }
    }

    /**
     * 認証用のリダイレクト先URLを返却するメソッド。
     * 
     * OAuth認証では、まずリクエストトークンを認証元に要求する必要がある。
     * リクエストトークンは、リダイレクト用のURLに混ぜ込まれて、
     * ユーザをTwitterの認証画面に連れていく。
     * 
     * @return リクエストトークンを含んだリダイレクトURL
     */
    public String requestReqToken() {
        try {
            requestToken = service.getRequestToken();
            return service.getAuthorizationUrl(requestToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Twitterの認証画面のログイン結果を元に、Twitterからアクセストークンを払い出してもらうメソッド。
     * 
     * @param oauthToken リクエストトークン。
     * @param verifier アクセストークンを要求するための認証文字列。
     * @return アクセストークン登録済みのTwitteredのTwitterClient。
     */
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
    /**
     * Twitteredのクライアントを返却するメソッド。
     * 
     * @param oauthToken リクエストトークン。
     * @param verifier アクセストークンを要求するための認証文字列。
     * @return アクセストークン登録済みのTwitteredのTwitterClient。もし認証されていない場合、戻り値はnullになる。
     */
    public TwitterClient getTwitterClient(){
        return twitterClient;
    }

    /**
     * OAuth認証後の通信では、アクセストークン等に基づいてリクエストに署名を付与する必要があるため、
     * その署名及びその後のリクエスト送信を実施するメソッド。
     * 
     * @param request 署名前のリクエスト。
     * @return 署名後、送信したリクエストのレスポンス。
     */
    public Response executeRequest(OAuthRequest request){
        service.signRequest(accessToken, request);
        try {
            return service.execute(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
}
