package com.example.galleryfilter.service;

import com.example.galleryfilter.model.FilteredTweets;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * バックエンドのPythonのイラスト分類APIにアクセスし、画像つきツイートの分類を行うことを目的とする。
 */
public interface PhotoFilteringService {
    /**
     * ツイート配列のJSONをそのまま受け取って、
     * 画像ありツイートの抽出から、バックエンドのイラスト分類APIへの照会、
     * その結果を基にしたイラスト/写真ツイートの振り分けまでを行うメソッド。
     * 
     * @param tweets ツイートの配列。
     * @return イラストと写真に分類されたツイートの各リスト。
     */
    public FilteredTweets filteringTweets(ArrayNode tweets);
}
