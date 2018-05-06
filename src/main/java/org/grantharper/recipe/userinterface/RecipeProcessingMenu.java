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

@Component
public class RecipeProcessingMenu
{
  private static final Logger logger = LogManager.getLogger(RecipeProcessingMenu.class);

  private static final String PROCESS_IMAGE_TO_TEXT_OPTION = "Process image into text";
  private static final String PROCESS_TEXT_TO_JSON_OPTION = "Process text into JSON";
  private static final String PROCESS_JSON_TO_ELASTICSEARCH_OPTION = "Load JSON to the Elasticsearch Index";

  public static void main(String[] args)
  {
    runProcessingMenu();
  }

  public static void runProcessingMenu()
  {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      PrintStream ps = System.out;
      String processingUserSelection = null;
      while (!isValidProcessingOption(processingUserSelection)) {
        ps.println("Recipe Processor Menu");
        ps.println("Select a processing step");
        ps.println("1) " + PROCESS_IMAGE_TO_TEXT_OPTION);
        ps.println("2) " + PROCESS_TEXT_TO_JSON_OPTION);
        ps.println("3) " + PROCESS_JSON_TO_ELASTICSEARCH_OPTION);
        processingUserSelection = br.readLine();
      }
      ps.println("You selected processing step: " + processingUserSelection);
      ps.println("Input a file or directory to process if you wish to override default settings. Default directories are below" );
      ps.println(PROCESS_IMAGE_TO_TEXT_OPTION + ": data/input");
      ps.println(PROCESS_TEXT_TO_JSON_OPTION + ": data/output/text");
      ps.println(PROCESS_JSON_TO_ELASTICSEARCH_OPTION + ": data/output/json");
      String targetSourceUserInput = br.readLine();
      Path datasourceInputPath = determineSource(targetSourceUserInput, RecipeMenuOption.getOptionFromIndex(Integer.valueOf(processingUserSelection)));
      ps.println("You selected the source: " + datasourceInputPath);
    } catch (IOException e) {
      logger.error("Failed to read user input", e);
    }
  }

  public static boolean isValidProcessingOption(String processingUserSelection)
  {
    return !StringUtils.isEmpty(processingUserSelection) && (processingUserSelection.equals("1") || processingUserSelection.equals("2") || processingUserSelection.equals("3"));
  }


  static Path determineSource(String datasourceUserInput, RecipeMenuOption menuOption)
  {
    if (StringUtils.isEmpty(datasourceUserInput)) {
      switch (menuOption) {
        case PROCESS_IMAGE_TO_TEXT:
          return Paths.get("data/input");
        case PROCESS_TEXT_TO_JSON:
          return Paths.get("data/output/text");
        case PROCESS_JSON_TO_ELASTICSEARCH:
          return Paths.get("data/output/json");
      }
      throw new RuntimeException("invalid menu option");
    }
    else return Paths.get(datasourceUserInput);
  }

}
