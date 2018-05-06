package org.grantharper.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;
import org.grantharper.recipe.processor.ConvertImageToText;
import org.grantharper.recipe.processor.ConvertTextToJson;
import org.grantharper.recipe.processor.LoadElasticsearch;
import org.grantharper.recipe.userinterface.RecipeMenuUserSelection;
import org.grantharper.recipe.userinterface.RecipeProcessingMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@ComponentScan
public class RecipeApplication
{
  private static final Logger logger = LogManager.getLogger(RecipeApplication.class);

  private ConvertImageToText convertImageToText;
  private RestHighLevelClient restHighLevelClient;
  private ConvertTextToJson convertTextToJson;
  private LoadElasticsearch loadElasticsearch;
  private RecipeProcessingMenu recipeProcessingMenu;

  @Autowired
  public void setConvertImageToText(ConvertImageToText convertImageToText)
  {
    this.convertImageToText = convertImageToText;
  }

  @Autowired
  public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient)
  {
    this.restHighLevelClient = restHighLevelClient;
  }

  @Autowired
  public void setConvertTextToJson(ConvertTextToJson convertTextToJson)
  {
    this.convertTextToJson = convertTextToJson;
  }

  @Autowired
  public void setLoadElasticsearch(LoadElasticsearch loadElasticsearch)
  {
    this.loadElasticsearch = loadElasticsearch;
  }

  @Autowired
  public void setRecipeProcessingMenu(RecipeProcessingMenu recipeProcessingMenu)
  {
    this.recipeProcessingMenu = recipeProcessingMenu;
  }

  public static void main(String[] args)
  {
    logger.info("OCR App Running");
    ApplicationContext context =
            new AnnotationConfigApplicationContext(RecipeApplication.class);
    RecipeApplication recipeApplication = context.getBean(RecipeApplication.class);
    recipeApplication.runApplication();
    recipeApplication.closeResources();

  }

  void runApplication()
  {
    boolean continueProcessing = true;
    while(continueProcessing) {
      try {
        RecipeMenuUserSelection recipeMenuUserSelection = getUserInput();
        switch (recipeMenuUserSelection.getRecipeMenuOption()) {
          case PROCESS_IMAGE_TO_TEXT:
            runImageConversion(recipeMenuUserSelection);
            break;
          case PROCESS_TEXT_TO_JSON:
            runTextToJsonConversion(recipeMenuUserSelection);
            break;
          case PROCESS_JSON_TO_ELASTICSEARCH:
            runElasticsearchLoad(recipeMenuUserSelection);
            break;
          case EXIT:
            continueProcessing = false;
            break;
        }
      } catch (Exception e) {
        logger.error("Processing error", e);
      }
    }
  }

  RecipeMenuUserSelection getUserInput() throws IOException
  {
    return recipeProcessingMenu.runProcessingMenu();
  }

  void runImageConversion(RecipeMenuUserSelection recipeMenuUserSelection)
  {
    logger.info("executing conversion of images to text");
    this.convertImageToText.convert(recipeMenuUserSelection);
  }

  void runTextToJsonConversion(RecipeMenuUserSelection recipeMenuUserSelection)
  {
    logger.info("executing conversion of text to json");
    this.convertTextToJson.convert(recipeMenuUserSelection);
  }

  void runElasticsearchLoad(RecipeMenuUserSelection recipeMenuUserSelection)
  {
    logger.info("loading json to elasticsearch index");
    this.loadElasticsearch.load(recipeMenuUserSelection);
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
