package org.grantharper.recipe.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.converter.FormatConverter;
import org.grantharper.recipe.ocr.OCRException;
import org.grantharper.recipe.serializer.FileUtils;
import org.grantharper.recipe.userinterface.RecipeMenuUserSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ConvertImageToText
{

  private static final Logger logger = LogManager.getLogger(ConvertImageToText.class);

  private FormatConverter imageToTextConverter;

  private Path txtOutputDir;

  @Value("${outputDir.txt}")
  void setTxtOutputDir(String txtOutputDir)
  {
    this.txtOutputDir = Paths.get(txtOutputDir);
  }

  @Autowired
  public ConvertImageToText(FormatConverter imageToTextConverter)
  {
    this.imageToTextConverter = imageToTextConverter;
  }

  public void convert(RecipeMenuUserSelection recipeMenuUserSelection)
  {
    if (Files.exists(this.txtOutputDir)) {
      FileUtils.cleanDirectory(this.txtOutputDir);
    }else {
      FileUtils.createDirectory(this.txtOutputDir);
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

  void convertDirectory(Path sourceDirectory)
  {
    try {
      Files.list(sourceDirectory)
              .filter(p -> p.getFileName()
                      .toString()
                      .endsWith(".jpg"))
              .forEach(this::convertFile);
    } catch (IOException e) {
      logger.error("Input/Output directory problem", e);
    }

  }

  void convertFile(Path imageFile)
  {

    try {
      logger.info("processing " + imageFile.getFileName().toString());
      Path textFile = imageToTextConverter.convert(imageFile, this.txtOutputDir);

    } catch (OCRException e) {
      logger.error("Error with OCR processing: " + imageFile, e);
    } catch (IOException e) {
      logger.error("Image to text conversion process failed: " + imageFile, e);
    }
  }


}
