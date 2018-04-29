package org.grantharper.recipe.serializer;

public class FileUtils
{
  
  private FileUtils() {}

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
  
  private static String changeFileExtensionToOtherExtension(String originalFilename, String otherExtension) {
    return originalFilename.substring(0, originalFilename.length() -4) + otherExtension;
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
