package com.example.galleryfilter.twitter;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import io.github.redouane59.twitter.dto.others.RequestToken;
import io.github.redouane59.twitter.helpers.RequestHelper;
import io.github.redouane59.twitter.helpers.RequestHelperV2;
import io.github.redouane59.twitter.helpers.URLHelper;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth10aService;

public class TwitterClient extends io.github.redouane59.twitter.TwitterClient{

  public TwitterClient(TwitterCredentials credentials) {
    this(credentials, new ServiceBuilder(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey()));
  }

  public TwitterClient(TwitterCredentials credentials, HttpClient httpClient) {
    this(credentials,
         new ServiceBuilder(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey()).httpClient(httpClient));
  }

  public TwitterClient(TwitterCredentials credentials, HttpClient httpClient, HttpClientConfig config) {
    this(credentials, new ServiceBuilder(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey())
                                                                 .httpClient(httpClient).httpClientConfig(config));
  }

  public TwitterClient(TwitterCredentials credentials, ServiceBuilder serviceBuilder) {
    this(credentials, serviceBuilder.apiKey(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey())
                                    .build(TwitterApi.instance()));
  }

  public TwitterClient(TwitterCredentials credentials, OAuth10aService service) {
    super(credentials, service);
    this.setTwitterCredentials(credentials);
    this.setRequestHelperV1(new RequestHelper(credentials, service));
    this.setRequestHelperV2(new RequestHelperV2(credentials, service));
  }

    @Override
    public RequestToken getOAuth1AccessToken(RequestToken requestToken, String pinCode) {
        String              url        = URLHelper.GET_OAUTH1_ACCESS_TOKEN_URL;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("oauth_token", requestToken.getOauthToken());
        parameters.put("oauth_verifier", pinCode);
        // parameters.put("oauth_consumer_key", getTwitterCredentials().getApiKey());
        
        // parameters.put("oauth_consumer_key", getTwitterCredentials().getApiKey());
        String stringResponse = this.getRequestHelperV1().postRequest(url, parameters, String.class).orElseThrow(NoSuchElementException::new);
        return new RequestToken(stringResponse);
    }
}
