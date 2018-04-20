package org.grantharper.recipe.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.grantharper.recipe.domain.Recipe;

import java.io.File;
import java.io.IOException;

public class ElasticSearchLoader
{

  private static final String RECIPE_INDEX_NAME = "recipe";
  private RestHighLevelClient elasticClient;
  private ObjectMapper objectMapper = new ObjectMapper();

  public ElasticSearchLoader(){
    elasticClient = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));
  }

  public void close(){
    try {
      elasticClient.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void loadRecipeJson(String pathname){

    try {
      IndexRequest indexRequest = new IndexRequest(RECIPE_INDEX_NAME, "json");
      Recipe recipe = objectMapper.readValue(new File(pathname), Recipe.class);
      String jsonString = objectMapper.writeValueAsString(recipe);
      indexRequest.source(jsonString, XContentType.JSON);
      IndexResponse indexResponse = elasticClient.index(indexRequest);
      System.out.println(indexResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void searchForRecipeByIngredientName(){
    SearchRequest searchRequest = new SearchRequest(RECIPE_INDEX_NAME);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    searchRequest.source(searchSourceBuilder);

    SearchResponse response = performSearch(searchRequest);
    System.out.println("overall search result= " + response);
  }

  SearchResponse performSearch(SearchRequest searchRequest) {
    SearchResponse response;
    try {
      response = elasticClient.search(searchRequest);
      return response;
    } catch (IOException e) {
      System.out.println("search error");
      return null;
    }

  }
}
