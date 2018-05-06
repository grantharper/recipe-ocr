package org.grantharper.recipe.userinterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class RecipeProcessingMenu
{
  private static final Logger logger = LogManager.getLogger(RecipeProcessingMenu.class);

  public static void main(String[] args)
  {
    try{
      RecipeProcessingMenu recipeProcessingMenu = new RecipeProcessingMenu();
      recipeProcessingMenu.runProcessingMenu();
    }catch (IOException e) {
      logger.error("Failed to read user input", e);
    }
  }

  public RecipeMenuUserSelection runProcessingMenu() throws IOException
  {
    RecipeMenuUserSelection.RecipeMenuUserSelectionBuilder builder =
            new RecipeMenuUserSelection.RecipeMenuUserSelectionBuilder();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      PrintStream ps = System.out;
      RecipeMenuOption processingUserSelection = null;
      while (processingUserSelection == null) {
        ps.println("Recipe Processor Menu");
        ps.println("Select a processing step");
        Arrays.stream(RecipeMenuOption.values()).forEach(o -> {
          ps.println(o.getOptionNumber() + ") " + o.getMenuListing());
        });
        processingUserSelection = RecipeMenuOption.getOptionFromMenuOption(br.readLine());
      }
      ps.println("You selected processing step: " + processingUserSelection);
      builder.setRecipeMenuOption(processingUserSelection);
      Path datasourceInputPath = null;
      while (datasourceInputPath == null || !datasourceInputPath.toFile().exists()) {
        ps.println("Input a file or directory to process: (default) "
                + processingUserSelection.getDefaultSourceDirectory());
        String targetSourceUserInput = br.readLine();
        datasourceInputPath = determineSource(targetSourceUserInput, processingUserSelection);
      }
      ps.println("You selected the data source: " + datasourceInputPath);
      builder.setSourcePath(datasourceInputPath);
    }
    return builder.build();
  }

  static Path determineSource(String datasourceUserInput, RecipeMenuOption menuOption)
  {
    if (StringUtils.isEmpty(datasourceUserInput)) {
      return Paths.get(menuOption.getDefaultSourceDirectory());
    } else return Paths.get(datasourceUserInput);
  }

}
