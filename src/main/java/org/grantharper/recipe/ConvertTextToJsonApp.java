package org.grantharper.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.domain.Recipe;
import org.grantharper.recipe.parser.RecipeParserSurLaTable;
import org.grantharper.recipe.serializer.FileUtils;
import org.grantharper.recipe.serializer.RecipeJsonCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvertTextToJsonApp
{

  private static final Logger logger = LogManager.getLogger(ConvertTextToJsonApp.class);

  private final RecipeJsonCreator recipeJsonCreator;

  private String jsonOutputDir;
  private String txtOutputDir;

  @Value("${outputDir.json}")
  void setJsonOutputDir(String jsonOutputDir)
  {
    this.jsonOutputDir = jsonOutputDir;
  }

  @Value("${outputDir.txt}")
  void setTxtOutputDir(String txtOutputDir)
  {
    this.txtOutputDir = txtOutputDir;
  }

  @Autowired
  public ConvertTextToJsonApp(RecipeJsonCreator recipeJsonCreator)
  {
    this.recipeJsonCreator = recipeJsonCreator;
  }

  public void convertDirectory()
  {
    try {
      Files.createDirectories(Paths.get(this.jsonOutputDir));
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

      Recipe recipe = new RecipeParserSurLaTable(textFile.getFileName().toString()).parse(recipeText);
      String json = recipeJsonCreator.generateOutput(recipe);
      List<String> output = Arrays.asList(json);

      Files.write(Paths.get(this.jsonOutputDir, FileUtils.changeFileExtensionToJson(textFile.getFileName()
                      .toString())), output, Charset.defaultCharset(),
              StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    }  catch (IOException e) {
      logger.error("Text to json conversion process failed: " + textFile, e);
    }
  }


}
