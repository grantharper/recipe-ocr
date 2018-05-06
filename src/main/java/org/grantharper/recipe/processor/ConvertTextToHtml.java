package org.grantharper.recipe.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.serializer.*;
import org.springframework.beans.factory.annotation.Value;

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

  private String htmlOutputDir;
  private String txtOutputDir;

  @Value("${outputDir.html}")
  void setHtmlOutputDir(String htmlOutputDir)
  {
    this.htmlOutputDir = htmlOutputDir;
  }

  @Value("${outputDir.txt}")
  void setTxtOutputDir(String txtOutputDir)
  {
    this.txtOutputDir = txtOutputDir;
  }


  public void convertDirectory()
  {
    try {
      Files.createDirectories(Paths.get(this.htmlOutputDir));
      Files.list(Paths.get(this.txtOutputDir))
              .filter(p -> p.getFileName()
                      .toString()
                      .endsWith(".txt"))
              .forEach(this::convertFile);
    } catch (IOException e) {
      logger.error("Input/Output directory problem", e);
    }

  }

  void convertFile(Path textFile)
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

      Files.write(Paths.get(this.htmlOutputDir, htmlFilename), htmlPage, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
