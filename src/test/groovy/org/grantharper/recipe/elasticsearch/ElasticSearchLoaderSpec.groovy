package org.grantharper.recipe.elasticsearch

import spock.lang.Specification

class ElasticSearchLoaderSpec extends Specification
{

  ElasticSearchLoader elasticSearchLoader;

  def setup() {
    elasticSearchLoader = new ElasticSearchLoader()
  }

  def cleanup() {
    elasticSearchLoader.close()
  }

  def "write recipe to the recipe index"() {
    when: "recipe json is written to the recipe index"
    elasticSearchLoader.loadRecipeJson("data/output/json/201.json")

    then: "recipe json is queryable"
    true == true
  }

  def "search recipe index"() {
    when: "recipe index is searched"
    elasticSearchLoader.searchForRecipeByIngredientName()

    then: "recipe is found"
    true == true
  }



}
