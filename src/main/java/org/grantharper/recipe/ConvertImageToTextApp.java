package org.grantharper.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.converter.FormatConverter;
import org.grantharper.recipe.ocr.OCRException;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public ConvertImageToTextApp(FormatConverter jpegToPngConverter, FormatConverter pngToTextConverter)
  {
    this.jpegToPngConverter = jpegToPngConverter;
    this.pngToTextConverter = pngToTextConverter;
  }

  public void execute()
  {
    try {
      Files.createDirectories(Paths.get(AppConfig.PNG_OUTPUT_DIR));
      Files.createDirectories(Paths.get(AppConfig.TEXT_OUTPUT_DIR));
      Files.list(Paths.get(AppConfig.JPG_INPUT_DIR))
              .filter(p -> p.getFileName()
                      .toString()
                      .endsWith(".jpg"))
              .forEach(this::performOCR);
    } catch (IOException e) {
      logger.error("Input/Output directory problem", e);
    }

  }

  void performOCR(Path imageFile)
  {

    try {
      logger.info("processing " + imageFile.getFileName().toString());
      Path pngImageFile = jpegToPngConverter.convert(imageFile, Paths.get(AppConfig.PNG_OUTPUT_DIR));
      Path textFile = pngToTextConverter.convert(pngImageFile, Paths.get(AppConfig.TEXT_OUTPUT_DIR));

    } catch (OCRException e) {
      logger.error("Error with OCR processing: " + imageFile, e);
    } catch (IOException e) {
      logger.error("Image to text conversion process failed: " + imageFile, e);
    }
  }


}
