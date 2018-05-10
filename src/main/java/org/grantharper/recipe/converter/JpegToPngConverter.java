package org.grantharper.recipe.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.serializer.FileUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class JpegToPngConverter implements FormatConverter
{

  private static final Logger logger = LogManager.getLogger(JpegToPngConverter.class);

  public Path convert(Path jpegFile, Path outputDir) throws IOException
  {
    String pngFilename = FileUtils.changeFileExtensionToPng(jpegFile.getFileName().toString());
    File outputFile = new File(outputDir.toString() + "/" + pngFilename);
    BufferedImage bufferedImage = ImageIO.read(jpegFile.toFile());
    ImageIO.write(bufferedImage, "png", outputFile);
    logger.info("created image file=" + outputFile.getName());
    return outputFile.toPath();
  }

}
