package org.grantharper.recipe.serializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.ConvertImageToTextApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils
{

  private static final Logger logger = LogManager.getLogger(FileUtils.class);

  private FileUtils()
  {
  }

  public static String changeFileExtensionToHtml(String pngFilename)
  {
    return changeFileExtensionToOtherExtension(pngFilename, ".html");
  }

  public static String changeFileExtensionToTxt(String pngFilename)
  {
    return changeFileExtensionToOtherExtension(pngFilename, ".txt");
  }

  public static String changeFileExtensionToJson(String pngFilename)
  {
    return changeFileExtensionToOtherExtension(pngFilename, ".json");
  }

  private static String changeFileExtensionToOtherExtension(String originalFilename, String otherExtension)
  {
    return originalFilename.substring(0, originalFilename.length() - 4) + otherExtension;
  }

  public static String changeJpegExtensionToPng(String jpegFilename)
  {
    return changeJpegExtensionToOtherExtension(jpegFilename, ".png");
  }

  public static void cleanDirectory(Path directory)
  {
    try {
      List<Path> files = Files.list(directory).collect(Collectors.toList());
      for (Path file : files) {
        Files.delete(file);
      }
    } catch (IOException e) {
      logger.error("Failed deleting files in directory=" + directory.toString(), e);
      throw new RuntimeException("Directory cleanup failed");
    }

  }

  public static void createDirectory(Path directory)
  {
    try {
      Files.createDirectories(directory);
    } catch (IOException e) {
      logger.error("Failed creating directory=" + directory.toString(), e);
      throw new RuntimeException("Directory creation failed");
    }
  }

  private static String changeJpegExtensionToOtherExtension(String jpegFilename, String otherExtension)
  {

    int truncationLength = 5;

    if (jpegFilename.endsWith(".jpg")) {
      truncationLength = 4;
    }

    return jpegFilename.substring(0, jpegFilename.length() - truncationLength) + otherExtension;
  }

}
