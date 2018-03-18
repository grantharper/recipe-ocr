package org.grantharper.recipe.serializer;

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
}
