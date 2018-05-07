package org.grantharper.recipe.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.ocr.OCRExecutor;
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
import java.util.Arrays;

@Component
public class PngToTextConverter implements FormatConverter
{

  private static final Logger logger = LogManager.getLogger(PngToTextConverter.class);

  private final OCRExecutor ocrExecutor;
  private final Rectangle imageFileViewport;

  @Autowired
  public PngToTextConverter(OCRExecutor ocrExecutor, Rectangle imageFileViewport)
  {
    this.ocrExecutor = ocrExecutor;
    this.imageFileViewport = imageFileViewport;
  }

  @Override
  public Path convert(Path inputImage, Path outputDirectory) throws IOException
  {
    logger.info("performing OCR: " + inputImage.getFileName().toString());
    String recipeText = this.ocrExecutor.performTargetedOCR(inputImage, this.imageFileViewport);
    //remove double newline characters which make the file longer than necessary
    recipeText = recipeText.replace("\n\n", "\n");
    //TODO: make the extension change more generic so that it doesn't matter the file format
    Path outputTextFilePath = Paths.get(outputDirectory.toString(), FileUtils.changeFileExtensionToTxt(inputImage.getFileName()
            .toString()));


    Files.write(outputTextFilePath,
            Arrays.asList(FileUtils.removeExtension(inputImage.getFileName().toString()), recipeText),
            Charset.forName("UTF-8"),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    logger.info("created text file=" + outputTextFilePath.toString());
    return outputTextFilePath;

  }

}
