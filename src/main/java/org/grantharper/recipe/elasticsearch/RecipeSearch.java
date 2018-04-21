package org.grantharper.recipe.elasticsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class RecipeSearch
{

  private static final Logger logger = LogManager.getLogger(RecipeSearch.class);

  private ElasticSearchClient elasticSearchClient;

  @Autowired
  public void setElasticSearchClient(ElasticSearchClient elasticSearchClient)
  {
    this.elasticSearchClient = elasticSearchClient;
  }

  SearchHits searchByIngredient(String ingredientSearch){
    SearchRequest searchRequest = new SearchRequest(ElasticSearchClient.RECIPE_INDEX_NAME);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.boolQuery().must(
            QueryBuilders.termQuery("ingredients", ingredientSearch))
            .must(QueryBuilders.termQuery("book", "Sur La Table")));
    SearchResponse searchResponse = elasticSearchClient.performSearch(searchRequest);
    logger.info(searchResponse);
    SearchHits hits = searchResponse.getHits();
    return hits;
  }

  void finalAllRecipes(){
    SearchRequest searchRequest = new SearchRequest(ElasticSearchClient.RECIPE_INDEX_NAME);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    searchRequest.source(searchSourceBuilder);

    SearchResponse response = elasticSearchClient.performSearch(searchRequest);
    System.out.println("overall search result= " + response);
  }



}
