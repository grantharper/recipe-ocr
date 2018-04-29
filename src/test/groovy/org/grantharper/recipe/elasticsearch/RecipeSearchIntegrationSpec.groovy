package org.grantharper.recipe.elasticsearch

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.search.SearchHits
import spock.lang.Specification

class RecipeSearchIntegrationSpec extends Specification
{
  ElasticSearchClient elasticSearchClient
  RecipeSearch recipeSearch

  def setup() {
    elasticSearchClient = new ElasticSearchClient(new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http"))))
    recipeSearch = new RecipeSearch(elasticSearchClient)

  }

  def cleanup() {
    elasticSearchClient.close()
  }

  def "search recipe index"() {
    when: "recipe index is searched"
    SearchHits searchHits = recipeSearch.searchRecipeIndexByIngredients("flour")

    then: "recipe is found"
    searchHits.hits != null
    searchHits.hits.length >= 1
  }

}
