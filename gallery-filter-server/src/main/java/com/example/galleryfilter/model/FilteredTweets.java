package com.example.galleryfilter.model;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2D(イラスト)と3D(写真)で分類したツイートをそれぞれ保持するDTOクラス。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteredTweets {
    private List<JsonNode> tweetsWith2D;
    private List<JsonNode> tweetsWith3D;
}
