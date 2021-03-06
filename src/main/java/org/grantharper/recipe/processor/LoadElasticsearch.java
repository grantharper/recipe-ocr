package org.grantharper.recipe.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.elasticsearch.RecipeLoad;
import org.grantharper.recipe.userinterface.RecipeMenuUserSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LoadElasticsearch
{

  private static final Logger logger = LogManager.getLogger(LoadElasticsearch.class);

  private final RecipeLoad recipeLoad;

  @Autowired
  public LoadElasticsearch(RecipeLoad recipeLoad)
  {
    this.recipeLoad = recipeLoad;
  }

  public void load(RecipeMenuUserSelection recipeMenuUserSelection)
  {

    Path sourcePath = recipeMenuUserSelection.getSourcePath();
    if (Files.isDirectory(sourcePath)) {
      loadDirectory(sourcePath);
    } else if (Files.isRegularFile(sourcePath)) {
      loadFile(sourcePath);
    } else {
      throw new RuntimeException("Invalid source path: " + sourcePath.toString());
    }
  }

  void loadDirectory(Path sourceDirectory)
  {
    try {
      Files.list(sourceDirectory)
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
