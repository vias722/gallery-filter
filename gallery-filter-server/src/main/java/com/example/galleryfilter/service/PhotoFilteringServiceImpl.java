package com.example.galleryfilter.service;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.galleryfilter.model.FilteredTweets;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * PhotoFilteringServiceImplの実装。
 * バックエンドのPythonのイラスト分類APIにアクセスし、画像つきツイートの分類を行うことを目的とする。
 */
@Service
public class PhotoFilteringServiceImpl implements PhotoFilteringService{
    @Autowired
    RestTemplate restTemplate;

    @Value("${app.engine-location}")
    String engineUrl;

    /**
     * 
     */
    @Override
    public FilteredTweets filteringTweets(ArrayNode tweets) {
        ArrayList<JsonNode> list2d = new ArrayList<>();
        ArrayList<JsonNode> list3d = new ArrayList<>();
        var urls = new ArrayList<String>();
        var mediaTweets = new LinkedList<JsonNode>();
        for (JsonNode node : tweets) {
            try {
                var medias = node.get("extended_entities").get("media");
                var isPhoto = false;
                for (JsonNode media : medias) {
                    if (media.get("type").asText().equals("photo")){
                        urls.add(media.get("media_url").asText() + "?name=small");
                        isPhoto = true;
                    }
                }
                if (isPhoto) mediaTweets.add(node);
                
            } catch (Exception e) {
                continue;
            }
        }
        var results = restTemplate.postForObject(engineUrl, urls, ArrayNode.class);

        var mediaTweetIter = mediaTweets.iterator();
        var resultIter = results.iterator();

        if(!resultIter.hasNext()){
            return new FilteredTweets(list2d, list3d);
        }
        
        var result = resultIter.next();
        do {
            var mediaTweet = mediaTweetIter.next();
            var is2D = false;
            while (checkTweetContainsImageUrl(mediaTweet, result.get("url").asText())) {
                if (result.get("result").asText().equals("2d"))
                    is2D = true;
                if (!resultIter.hasNext()) break;
                result = resultIter.next();
            }
            if (is2D) list2d.add(mediaTweet);
            else list3d.add(mediaTweet);
        } while(mediaTweetIter.hasNext());

        return new FilteredTweets(list2d, list3d);
    }

    private boolean checkTweetContainsImageUrl(JsonNode tweet, String url){
        for (JsonNode media : tweet.get("extended_entities").get("media")) {
            if (url.equals(media.get("media_url").asText() + "?name=small")){
                return true;
            }
        }
        return false;
    }
}
