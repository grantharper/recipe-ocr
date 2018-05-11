package org.grantharper.recipe.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.ocr.OCRExecutor;
import org.grantharper.recipe.ocr.RectangleProvider;
import org.grantharper.recipe.serializer.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PngToTextConverter implements FormatConverter
{

  private static final Logger logger = LogManager.getLogger(PngToTextConverter.class);

  private final OCRExecutor ocrExecutor;

  private RectangleProvider rectangleProvider;

  @Autowired
  public PngToTextConverter(OCRExecutor ocrExecutor, RectangleProvider rectangleProvider)
  {
    this.ocrExecutor = ocrExecutor;
    this.rectangleProvider = rectangleProvider;
  }

  @Override
  public Path convert(Path inputImage, Path outputDirectory) throws IOException
  {
    logger.info("performing OCR: " + inputImage.getFileName().toString());
    List<Rectangle> rectangles = rectangleProvider.getRectangles();
    List<String> outputText = extractTextFromRectangles(inputImage, rectangles);
    Path outputTextFilePath = Paths.get(outputDirectory.toString(), FileUtils.changeFileExtensionToTxt(inputImage.getFileName()
            .toString()));
    Files.write(outputTextFilePath,
            outputText,
            Charset.forName("UTF-8"),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    logger.info("created text file=" + outputTextFilePath.toString());
    return outputTextFilePath;

  }

  List<String> extractTextFromRectangles(Path inputImage, List<Rectangle> rectangleList)
  {
    logger.info("performing OCR: " + inputImage.getFileName().toString());
    List<String> extractedInformation = new ArrayList<>();
    extractedInformation.add(FileUtils.removeExtension(inputImage.getFileName().toString()));
    for (Rectangle rectangle : rectangleList) {
      String text = this.ocrExecutor.performTargetedOCR(inputImage, rectangle);
      //remove double newline characters which make the file longer than necessary
      text = text.replace("\n\n", "\n");
      extractedInformation.add(text);
    }
    return extractedInformation;
  }

}
