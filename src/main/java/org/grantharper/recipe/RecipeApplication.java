package org.grantharper.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@ComponentScan
public class RecipeApplication
{
  private static final Logger logger = LogManager.getLogger(RecipeApplication.class);

  private ConvertImageToTextApp convertImageToTextApp;
  private RestHighLevelClient restHighLevelClient;
  private ConvertTextToJsonApp convertTextToJsonApp;
  private LoadElasticsearchApp loadElasticsearchApp;

  @Autowired
  public void setConvertImageToTextApp(ConvertImageToTextApp convertImageToTextApp)
  {
    this.convertImageToTextApp = convertImageToTextApp;
  }

  @Autowired
  public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient)
  {
    this.restHighLevelClient = restHighLevelClient;
  }

  @Autowired
  public void setConvertTextToJsonApp(ConvertTextToJsonApp convertTextToJsonApp)
  {
    this.convertTextToJsonApp = convertTextToJsonApp;
  }

  @Autowired
  public void setLoadElasticsearchApp(LoadElasticsearchApp loadElasticsearchApp)
  {
    this.loadElasticsearchApp = loadElasticsearchApp;
  }

  public static void main(String[] args)
  {
    logger.info("OCR App Running");
    ApplicationContext context =
            new AnnotationConfigApplicationContext(RecipeApplication.class);
    RecipeApplication recipeApplication = context.getBean(RecipeApplication.class);
    recipeApplication.runImageConversion();
    recipeApplication.runTextToJsonConversion();
    recipeApplication.runElasticsearchLoad();
    recipeApplication.closeResources();

  }

  void runImageConversion()
  {
    logger.info("executing conversion of images to text");
    this.convertImageToTextApp.execute();
  }

  void runTextToJsonConversion()
  {
    logger.info("executing conversion of text to json");
    this.convertTextToJsonApp.execute();
  }

  void runElasticsearchLoad()
  {
    logger.info("loading json to elasticsearch index");
    this.loadElasticsearchApp.execute();
  }

  void closeResources()
  {
    try {
      this.restHighLevelClient.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
