package org.grantharper.recipe;

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
public class ConvertImageToTextApp
{

  private static final Logger logger = LogManager.getLogger(ConvertImageToTextApp.class);

  private FormatConverter jpegToPngConverter;
  private FormatConverter pngToTextConverter;

  private Path txtOutputDir;
  private Path pngOutputDir;

  @Value("${outputDir.txt}")
  void setTxtOutputDir(String txtOutputDir)
  {
    this.txtOutputDir = Paths.get(txtOutputDir);
  }

  @Value("${outputDir.png}")
  void setPngOutputDir(String pngOutputDir)
  {
    this.pngOutputDir = Paths.get(pngOutputDir);
  }

  @Autowired
  public ConvertImageToTextApp(FormatConverter jpegToPngConverter, FormatConverter pngToTextConverter)
  {
    this.jpegToPngConverter = jpegToPngConverter;
    this.pngToTextConverter = pngToTextConverter;
  }

  public void convert(RecipeMenuUserSelection recipeMenuUserSelection)
  {
    if (Files.exists(this.pngOutputDir)) {
      FileUtils.cleanDirectory(this.pngOutputDir);
    }else {
      FileUtils.createDirectory(this.pngOutputDir);
    }
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
      Path pngImageFile = jpegToPngConverter.convert(imageFile, this.pngOutputDir);
      Path textFile = pngToTextConverter.convert(pngImageFile, this.txtOutputDir);

    } catch (OCRException e) {
      logger.error("Error with OCR processing: " + imageFile, e);
    } catch (IOException e) {
      logger.error("Image to text conversion process failed: " + imageFile, e);
    }
  }


}
