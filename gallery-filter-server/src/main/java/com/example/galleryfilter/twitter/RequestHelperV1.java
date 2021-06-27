package com.example.galleryfilter.twitter;

import java.util.Map;
import java.util.Optional;

import io.github.redouane59.twitter.helpers.RequestHelper;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

public class RequestHelperV1 extends RequestHelper{

    public RequestHelperV1(TwitterCredentials twitterCredentials, OAuth10aService service){
        super(twitterCredentials, service);
    }

    public <T> Optional<T> makeRequest(Verb verb, String url, Map<String, String> headers,
                                     Map<String, String> parameters, String body, boolean signRequired, Class<T> classType) {
    OAuthRequest request = new OAuthRequest(verb, url);
    if (headers != null) {
      for (Map.Entry<String, String> header : headers.entrySet()) {
        request.addHeader(header.getKey(), header.getValue());
      }
    }
    if (parameters != null) {
      for (Map.Entry<String, String> param : parameters.entrySet()) {
        request.addQuerystringParameter(param.getKey(), param.getValue());
      }
    }
    if (body != null && verb.isPermitBody()) {
      request.setPayload(body);
      if (!request.getHeaders().containsKey("Content-Type")) {
        request.addHeader("Content-Type", "application/json");
      }
    }
    if (parameters.get("oauth_verifier")!=null)
        request.addOAuthParameter("oauth_verifier", parameters.get("oauth_verifier"));
    return makeRequest(request, signRequired, classType);
  }
}
