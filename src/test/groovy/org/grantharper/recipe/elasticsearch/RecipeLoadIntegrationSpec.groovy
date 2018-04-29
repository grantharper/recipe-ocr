package org.grantharper.recipe.elasticsearch

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.rest.RestStatus
import spock.lang.Specification

class RecipeLoadIntegrationSpec extends Specification
{

  ElasticSearchClient elasticSearchClient
  RecipeLoad recipeLoad

  def setup() {
    elasticSearchClient = new ElasticSearchClient(new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http"))))
    recipeLoad = new RecipeLoad(elasticSearchClient, new ObjectMapper())
    recipeLoad.setRecipeIndexName("recipe-test")
    recipeLoad.setRecipeIndexType("doc")

  }

  def cleanup() {
    elasticSearchClient.close()
  }

  def "write recipe to the recipe index"() {
    when: "recipe json is written to the recipe index"
    IndexResponse indexResponse = recipeLoad.loadRecipeJson("src/test/resources/sample-recipe.json")

    then: "response is successful"
    indexResponse.status() == RestStatus.OK
  }

}
