package org.grantharper.recipe.elasticsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RecipeSearch
{

  private static final Logger logger = LogManager.getLogger(RecipeSearch.class);

  private ElasticSearchClient elasticSearchClient;

  @Autowired
  public RecipeSearch(ElasticSearchClient elasticSearchClient)
  {
    this.elasticSearchClient = elasticSearchClient;
  }

  public List<String> searchRecipesForIngredients(String ingredientSearch)
  {
    logger.info("performing search: " + ingredientSearch);

    SearchHits searchHits = searchRecipeIndexByIngredients(ingredientSearch);
    List<String> locatedRecipes = new ArrayList<>();

    for(SearchHit searchHit: searchHits.getHits()){
      Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
      String pageId = (String) sourceAsMap.get("pageId");
      String book = (String) sourceAsMap.get("book");
      String title = (String) sourceAsMap.get("title");
      String result = "title: " + title + ", location: " + book + "-" + pageId;
      logger.info(result);
      locatedRecipes.add(result);
    }
    return locatedRecipes;
  }

  SearchHits searchRecipeIndexByIngredients(String ingredientSearch){
    SearchRequest searchRequest = new SearchRequest(ElasticSearchClient.RECIPE_INDEX_NAME);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(createIngredientQuery(ingredientSearch));
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = elasticSearchClient.performSearch(searchRequest);
    logger.info(searchResponse);
    return searchResponse.getHits();
  }

  BoolQueryBuilder createIngredientQuery(String ingredientSearch)
  {
    List<TermQueryBuilder> booleanTerms = new ArrayList<>();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    String[] ingredientTerms = ingredientSearch.split(" ");
    for(String ingredientTerm: ingredientTerms)
    {
      boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("ingredients", ingredientTerm));
    }
    boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("book", "Sur La Table"));
    return boolQueryBuilder;
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
