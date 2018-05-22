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

@ComponentScan
public class SearchElasticsearchApp
{

  private static final Logger logger = LogManager.getLogger(SearchElasticsearchApp.class);

  private RecipeSearch recipeSearch;

  @Autowired
  public SearchElasticsearchApp(RecipeSearch recipeSearch)
  {
    this.recipeSearch = recipeSearch;
  }

  public static void main(String[] args)
  {
      ApplicationContext context =
              new AnnotationConfigApplicationContext(SearchElasticsearchApp.class);

      SearchElasticsearchApp searchElasticsearchApp = context.getBean(SearchElasticsearchApp.class);

      searchElasticsearchApp.performSearches();
      ElasticSearchClient elasticSearchClient = context.getBean(ElasticSearchClient.class);
      elasticSearchClient.close();

  }

  void performSearches(){
    try {
      List<String> results = recipeSearch.searchRecipesForIngredients("unsalted asparagus");
      logger.info(results);

    } catch (Exception e) {
      logger.error("search failure", e);
    }
  }



}
