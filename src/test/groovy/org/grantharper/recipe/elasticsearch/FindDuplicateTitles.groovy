package org.grantharper.recipe.elasticsearch

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.HttpHost
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.builder.SearchSourceBuilder
import spock.lang.Specification

class FindDuplicateTitles extends Specification
{

  RestHighLevelClient restHighLevelClient;

  def setup() {
    restHighLevelClient = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")))
  }

  def cleanup() {
    restHighLevelClient.close()
  }

  def "find recipes with duplicate titles"()
  {
    given: "Search request"
    SearchRequest searchRequest = new SearchRequest("recipe")

    when: "all recipes are pulled back"
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(300);
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    searchRequest.source(searchSourceBuilder);

    SearchResponse response = restHighLevelClient.search(searchRequest);
    println("overall search result= " + response);
    Set<String> recipeTitles = new HashSet<>()
    SearchHits searchHits = response.getHits()
    for(SearchHit searchHit: searchHits.getHits()){
      Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
      String pageId = (String) sourceAsMap.get("pageId");
      String book = (String) sourceAsMap.get("book");
      String title = (String) sourceAsMap.get("title");
      String result = "title: " + title + ", location: " + book + "-" + pageId;
      if (recipeTitles.add(title)) {
        //println result
      } else {
        println 'DUPLICATE: ' + result
      }
    }

    then:
    true == true

  }

}
