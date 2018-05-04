package org.grantharper.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.elasticsearch.RecipeLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LoadElasticsearchApp
{

  private static final Logger logger = LogManager.getLogger(LoadElasticsearchApp.class);

  private final RecipeLoad recipeLoad;
  private String jsonOutputDir;

  @Value("${outputDir.json}")
  void setJsonOutputDir(String jsonOutputDir)
  {
    this.jsonOutputDir = jsonOutputDir;
  }

  @Autowired
  public LoadElasticsearchApp(RecipeLoad recipeLoad)
  {
    this.recipeLoad = recipeLoad;
  }

  void loadDirectory()
  {
    try {
      Files.list(Paths.get(this.jsonOutputDir))
              .filter(p -> p.getFileName()
                      .toString()
                      .endsWith(".json"))
              .forEach(this::loadFile);
    } catch (IOException e) {
      logger.error("Error while loading to elasticsearch", e);
    }
  }

  void loadFile(Path jsonFile){
    logger.info("processing " + jsonFile.getFileName().toString());
    recipeLoad.loadRecipeJson(jsonFile);
  }
}
