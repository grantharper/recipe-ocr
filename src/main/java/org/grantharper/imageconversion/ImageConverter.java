package org.grantharper.imageconversion;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.grantharper.recipe.serializer.FileUtils;

public class ImageConverter
{

  
  Path convertJpegToPng(Path jpegFile, Path outputDir) {
    String pngFilename = FileUtils.changeJpegExtensionToPng(jpegFile.getFileName().toString());
    return Paths.get(outputDir.toString(), pngFilename);
  }
  
}
