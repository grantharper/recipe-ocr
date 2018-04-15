package org.grantharper.imageconversion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.grantharper.recipe.serializer.FileUtils;

import javax.imageio.ImageIO;

public class ImageConverter
{

  public Path convertJpegToPng(Path jpegFile, Path outputDir) {
    String pngFilename = FileUtils.changeJpegExtensionToPng(jpegFile.getFileName().toString());
    File outputFile = new File(outputDir.toString() + "/" + pngFilename);

    if(!outputFile.exists()){ //skip processing if file has already been created previously
      try {
        BufferedImage bufferedImage = ImageIO.read(jpegFile.toFile());
        ImageIO.write(bufferedImage, "png", outputFile);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("image conversion failed");
      }
    }

    return outputFile.toPath();
  }

}
