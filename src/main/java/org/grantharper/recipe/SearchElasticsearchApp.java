package org.grantharper.recipe;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.grantharper.recipe.domain.Recipe;
import org.grantharper.recipe.elasticsearch.ElasticSearchClient;
import org.grantharper.recipe.elasticsearch.RecipeLoad;
import org.grantharper.recipe.elasticsearch.RecipeSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan
public class SearchElasticsearchApp
{

  private static final Logger logger = LogManager.getLogger(SearchElasticsearchApp.class);

  private RecipeSearch recipeSearch;

  @Bean
  RestHighLevelClient restHighLevelClient(){
    return new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));
  }

  public static void main(String[] args)
  {
    ApplicationContext context =
            new AnnotationConfigApplicationContext(SearchElasticsearchApp.class);
    SearchElasticsearchApp searchElasticsearchApp = new SearchElasticsearchApp();
    searchElasticsearchApp.recipeSearch = context.getBean(RecipeSearch.class);

    searchElasticsearchApp.performSearches();
    ElasticSearchClient elasticSearchClient = context.getBean(ElasticSearchClient.class);
    elasticSearchClient.close();
  }

  void performSearches(){
    List<String> results = recipeSearch.searchRecipesForIngredients("unsalted asparagus");

  }



}
