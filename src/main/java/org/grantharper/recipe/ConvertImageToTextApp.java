package org.grantharper.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.converter.FormatConverter;
import org.grantharper.recipe.ocr.OCRException;
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

  private String jpgInputDir;
  private String txtOutputDir;
  private String pngOutputDir;

  @Value("${outputDir.txt}")
  void setTxtOutputDir(String txtOutputDir)
  {
    this.txtOutputDir = txtOutputDir;
  }

  @Value("${inputDir}")
  void setJpgInputDir(String jpgInputDir)
  {
    this.jpgInputDir = jpgInputDir;
  }

  @Value("${outputDir.png}")
  void setPngOutputDir(String pngOutputDir)
  {
    this.pngOutputDir = pngOutputDir;
  }

  @Autowired
  public ConvertImageToTextApp(FormatConverter jpegToPngConverter, FormatConverter pngToTextConverter)
  {
    this.jpegToPngConverter = jpegToPngConverter;
    this.pngToTextConverter = pngToTextConverter;
  }

  public void execute()
  {
    try {
      Files.createDirectories(Paths.get(this.pngOutputDir));
      Files.createDirectories(Paths.get(this.txtOutputDir));
      Files.list(Paths.get(this.jpgInputDir))
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
      Path pngImageFile = jpegToPngConverter.convert(imageFile, Paths.get(this.pngOutputDir));
      Path textFile = pngToTextConverter.convert(pngImageFile, Paths.get(this.txtOutputDir));

    } catch (OCRException e) {
      logger.error("Error with OCR processing: " + imageFile, e);
    } catch (IOException e) {
      logger.error("Image to text conversion process failed: " + imageFile, e);
    }
  }


}
