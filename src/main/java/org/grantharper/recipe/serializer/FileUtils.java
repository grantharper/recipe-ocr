package org.grantharper.recipe.serializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils
{

  private static final Logger logger = LogManager.getLogger(FileUtils.class);

  private FileUtils()
  {
  }

  public static String changeFileExtensionToHtml(String filename)
  {
    return changeExtension(filename, ".html");
  }

  public static String changeFileExtensionToTxt(String filename)
  {
    return changeExtension(filename, ".txt");
  }

  public static String changeFileExtensionToJson(String filename)
  {
    return changeExtension(filename, ".json");
  }

  public static String changeFileExtensionToPng(String filename)
  {
    return changeExtension(filename, ".png");
  }

  public static String removeExtension(String filename)
  {
    int extensionIndex = filename.lastIndexOf(".");
    return filename.substring(0, extensionIndex);
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

  static String changeExtension(String filename, String otherExtension)
  {
    int extensionIndex = filename.lastIndexOf(".");
    return filename.substring(0, extensionIndex).concat(otherExtension);
  }

}
