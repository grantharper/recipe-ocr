package org.grantharper.recipe.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.domain.Recipe;
import org.grantharper.recipe.parser.RecipeParser;
import org.grantharper.recipe.serializer.FileUtils;
import org.grantharper.recipe.serializer.RecipeJsonCreator;
import org.grantharper.recipe.userinterface.RecipeMenuUserSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvertTextToJson
{

  private static final Logger logger = LogManager.getLogger(ConvertTextToJson.class);

  private final RecipeJsonCreator recipeJsonCreator;

  private Path jsonOutputDir;
  private String recipeParserImplementationClassName;

  @Value("${outputDir.json}")
  void setJsonOutputDir(String jsonOutputDir)
  {
    this.jsonOutputDir = Paths.get(jsonOutputDir);
  }

  @Value("${recipeParserImplementationClass}")
  public void setRecipeParserImplementationClassName(String recipeParserImplementationClassName)
  {
    this.recipeParserImplementationClassName = recipeParserImplementationClassName;
    try {
      Class<?> recipeParserClass = Class.forName(recipeParserImplementationClassName);
      List<Class<?>> recipeParserInterfaces = Arrays.asList(recipeParserClass.getInterfaces());
      Class<?>recipeParserParentClasses = recipeParserClass.getSuperclass();
      if (!recipeParserInterfaces.contains(Class.forName("org.grantharper.recipe.parser.RecipeParser"))
              && !recipeParserInterfaces.contains(Class.forName("org.grantharper.recipe.parser.RecipeParserExtended"))
              && !recipeParserParentClasses.equals(Class.forName("org.grantharper.recipe.parser.RecipeParserAbstract"))) {
        throw new RuntimeException("provided class does not implement the RecipeParser interface");
      }
    } catch (ClassNotFoundException e) {
      logger.error("Recipe parser class loading error", e);
      throw new RuntimeException("Invalid recipe parser class=" + recipeParserImplementationClassName);
    }

  }

  @Autowired
  public ConvertTextToJson(RecipeJsonCreator recipeJsonCreator)
  {
    this.recipeJsonCreator = recipeJsonCreator;

  }

  public void convert(RecipeMenuUserSelection recipeMenuUserSelection)
  {
    if (Files.exists(this.jsonOutputDir)) {
      FileUtils.cleanDirectory(this.jsonOutputDir);
    }else {
      FileUtils.createDirectory(this.jsonOutputDir);
    }

    Path sourcePath = recipeMenuUserSelection.getSourcePath();
    if (Files.isDirectory(sourcePath)) {
      convertDirectory(sourcePath);
    } else if (Files.isRegularFile(sourcePath)) {
      convertFile(sourcePath);
    } else {
      throw new RuntimeException("Invalid source path: " + sourcePath.toString());
    }
  }

  void convertDirectory(Path sourcePath)
  {
    try {
      Files.createDirectories(this.jsonOutputDir);
      Files.list(sourcePath)
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

      Recipe recipe = convertFileEnhanced(recipeParserImplementationClassName, recipeText);
      String json = recipeJsonCreator.generateOutput(recipe);
      List<String> output = Arrays.asList(json);

      Files.write(this.jsonOutputDir.resolve(FileUtils.changeFileExtensionToJson(textFile.getFileName()
                      .toString())), output, Charset.forName("UTF-8"),
              StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    }  catch (IOException e) {
      logger.error("Text to json conversion process failed: " + textFile, e);
    }
  }

  Recipe convertFileEnhanced(String className, String recipeText)
  {
    try {
      RecipeParser recipeParser = (RecipeParser) Class.forName(className).getConstructor().newInstance();
      Recipe recipe = recipeParser.parse(recipeText);
      return recipe;

    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
      logger.error("Recipe parser class loading error", e);
      throw new RuntimeException("Invalid recipe parser class=" + className);
    }
  }


}
