package org.grantharper.recipe.serializer;

import java.nio.file.Path;

public class FileUtils
{
  
  private FileUtils() {}

  public static String changePngExtensionToHtml(String pngFilename)
  {
    return changePngExtensionToOtherExtension(pngFilename, ".html");
  }
  
  public static String changePngExtensionToTxt(String pngFilename)
  {
    return changePngExtensionToOtherExtension(pngFilename, ".txt");
  }

  public static String changePngExtensionToJson(String pngFilename)
  {
    return changePngExtensionToOtherExtension(pngFilename, ".json");
  }
  
  private static String changePngExtensionToOtherExtension(String pngFilename, String otherExtension) {
    return pngFilename.substring(0, pngFilename.length() -4) + otherExtension;
  }

  public static String changeJpegExtensionToPng(String jpegFilename)
  {
    return changeJpegExtensionToOtherExtension(jpegFilename, ".png");
  }
  
  private static String changeJpegExtensionToOtherExtension(String jpegFilename, String otherExtension) {
    
    int truncationLength = 5;
    
    if(jpegFilename.endsWith(".jpg")) {
      truncationLength = 4;
    }
    
    return jpegFilename.substring(0, jpegFilename.length() - truncationLength) + otherExtension;
  }
}
