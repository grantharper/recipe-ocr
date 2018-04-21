package org.grantharper.recipe.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.grantharper.recipe.domain.Recipe;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public class RecipeLoad
{

  private static final Logger logger = LogManager.getLogger(RecipeLoad.class);

  private ElasticSearchClient elasticSearchClient;
  private ObjectMapper objectMapper;

  @Autowired
  public void setElasticSearchClient(ElasticSearchClient elasticSearchClient)
  {
    this.elasticSearchClient = elasticSearchClient;
  }

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper)
  {
    this.objectMapper = objectMapper;
  }

  IndexResponse loadRecipeJson(String pathname){

    String jsonString;
    Recipe recipe;

    try {
      recipe = objectMapper.readValue(new File(pathname), Recipe.class);
      jsonString = objectMapper.writeValueAsString(recipe);
    } catch (IOException e) {
      logger.error("recipe json parsing error", e);
      throw new RuntimeException();
    }

    String uniqueId = recipe.getBook().replace(" ", "") + recipe.getPageId();

    IndexRequest indexRequest = new IndexRequest(
            ElasticSearchClient.RECIPE_INDEX_NAME, ElasticSearchClient.TYPE,
            uniqueId);
    indexRequest.source(jsonString, XContentType.JSON);
    IndexResponse indexResponse = elasticSearchClient.performIndexLoad(indexRequest);
    logger.info(indexResponse);
    return indexResponse;
  }

}
