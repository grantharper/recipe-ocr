package org.grantharper.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.grantharper.recipe.elasticsearch.ElasticSearchClient;
import org.grantharper.recipe.elasticsearch.RecipeLoad;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
@ComponentScan
public class LoadElasticsearchApp
{

  private static final Logger logger = LogManager.getLogger(LoadElasticsearchApp.class);

  private RecipeLoad recipeLoad;

  @Bean
  RestHighLevelClient restHighLevelClient(){
    return new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));
  }

  @Bean
  ObjectMapper objectMapper(){
    return new ObjectMapper();
  }

  public static void main(String[] args)
  {
    ApplicationContext context =
            new AnnotationConfigApplicationContext(LoadElasticsearchApp.class);
    LoadElasticsearchApp loadElasticsearchApp = new LoadElasticsearchApp();
    loadElasticsearchApp.recipeLoad = context.getBean(RecipeLoad.class);
    loadElasticsearchApp.loadAllJsonToElasticSearch();
    ElasticSearchClient elasticSearchClient = context.getBean(ElasticSearchClient.class);
    elasticSearchClient.close();
  }

  void loadAllJsonToElasticSearch()
  {
    try {
      Files.list(Paths.get(Application.jsonOutputDir))
              .filter(p -> p.getFileName()
                      .toString()
                      .endsWith(".json"))
              .forEach(this::loadToElasticSearch);
    } catch (IOException e) {
      logger.error("Error while loading to elasticsearch", e);
    }
  }

  void loadToElasticSearch(Path jsonFile){
    logger.info("processing " + jsonFile.getFileName().toString());
    recipeLoad.loadRecipeJson(jsonFile);
  }
}
