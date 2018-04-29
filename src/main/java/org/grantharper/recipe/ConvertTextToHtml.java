package org.grantharper.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.serializer.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertTextToHtml
{

  private static final Logger logger = LogManager.getLogger(ConvertTextToHtml.class);

  public void execute()
  {
    try {
      Files.createDirectories(Paths.get(AppConfig.HTML_OUTPUT_DIR));
      Files.list(Paths.get(AppConfig.TEXT_OUTPUT_DIR))
              .filter(p -> p.getFileName()
                      .toString()
                      .endsWith(".txt"))
              .forEach(this::writeOcrResultToHtml);
    } catch (IOException e) {
      logger.error("Input/Output directory problem", e);
    }

  }

  void writeOcrResultToHtml(Path textFile)
  {

    try {
      logger.info("processing " + textFile.getFileName().toString());
      String recipeText = Files.readAllLines(textFile)
              .stream().collect(Collectors.joining("\n"));

      HtmlCreator htmlCreator = new HtmlCreatorImpl.Builder().setRecipeText(recipeText)
              .build();

      List<String> htmlPage = htmlCreator.generateHtmlPage();
      String htmlFilename = FileUtils.changeFileExtensionToHtml(textFile.getFileName()
              .toString());

      Files.write(Paths.get(AppConfig.HTML_OUTPUT_DIR, htmlFilename), htmlPage, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
