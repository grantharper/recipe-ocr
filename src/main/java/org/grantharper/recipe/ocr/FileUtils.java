package org.grantharper.recipe.ocr;

public class FileUtils
{
  
  private FileUtils() {}

  public static String changePngExtensionToHtml(String pngFilename)
  {
    return pngFilename.substring(0, pngFilename.length() - 4) + ".html";
  }
  
  public static String changePngExtensionToTxt(String pngFilename)
  {
    return pngFilename.substring(0, pngFilename.length() -4) + ".txt";
  }
}
