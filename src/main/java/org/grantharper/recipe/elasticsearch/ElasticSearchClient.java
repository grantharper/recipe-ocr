package org.grantharper.recipe.elasticsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticSearchClient
{

  private static final Logger logger = LogManager.getLogger(ElasticSearchClient.class);
  public static final String RECIPE_INDEX_NAME = "recipe";
  public static final String TYPE = "doc";

  private RestHighLevelClient elasticClient;

  @Autowired
  public void setElasticClient(RestHighLevelClient elasticClient)
  {
    this.elasticClient = elasticClient;
  }

  public void close(){
    try {
      elasticClient.close();
    } catch (IOException e) {
      logger.error("error closing REST high level client", e);
    }
  }

  IndexResponse performIndexLoad(IndexRequest indexRequest)
  {
    try {
      return elasticClient.index(indexRequest);
    } catch (IOException e) {
      logger.error("indexing failed", e);
      throw new RuntimeException();
    }
  }

  SearchResponse performSearch(SearchRequest searchRequest) {
    SearchResponse response;
    try {
      response = elasticClient.search(searchRequest);
      return response;
    } catch (IOException e) {
      logger.error("searching failed", e);
      throw new RuntimeException();
    }

  }
}
