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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class RecipeLoad
{

  private static final Logger logger = LogManager.getLogger(RecipeLoad.class);

  private ElasticSearchClient elasticSearchClient;
  private ObjectMapper objectMapper;

  private String recipeIndexName;
  private String recipeIndexType;

  @Value("${elasticsearch.index.name}")
  void setRecipeIndexName(String recipeIndexName)
  {
    this.recipeIndexName = recipeIndexName;
  }

  @Value("${elasticsearch.index.type}")
  void setRecipeIndexType(String recipeIndexType)
  {
    this.recipeIndexType = recipeIndexType;
  }

  @Autowired
  public RecipeLoad(ElasticSearchClient elasticSearchClient, ObjectMapper objectMapper)
  {
    this.elasticSearchClient = elasticSearchClient;
    this.objectMapper = objectMapper;
  }

  public void loadRecipeJson(Path jsonFilename){
    loadRecipeJson(jsonFilename.toString());
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

    String uniqueId = recipe.getBook().replace(" ", "") + recipe.getPageId().replace(" ", "");

    IndexRequest indexRequest = new IndexRequest(
            this.recipeIndexName, this.recipeIndexType,
            uniqueId);
    indexRequest.source(jsonString, XContentType.JSON);
    IndexResponse indexResponse = elasticSearchClient.performIndexLoad(indexRequest);
    logger.info(indexResponse);
    return indexResponse;
  }

}
