package org.grantharper.recipe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.grantharper.recipe.ocr.FileUtils;
import org.grantharper.recipe.ocr.HtmlCreator;
import org.grantharper.recipe.ocr.HtmlCreatorImpl;
import org.grantharper.recipe.ocr.OCRExecutor;

import net.sourceforge.tess4j.TesseractException;

public class Application
{

  OCRExecutor ocr = new OCRExecutor();

  public static void main(String[] args)
  {
    System.out.println("OCR App Running");
    Application app = new Application();

    try
    {
      Files.list(Paths.get("img/"))
          .filter(p -> p.getFileName().toString().endsWith(".png"))
          .forEach(p -> {
            app.performOCR(p);
          });
    } catch (IOException e)
    {
      System.out.println("File issue in image directory");
      e.printStackTrace();
    }

  }

  public void performOCR(Path imageFile)
  {

    try
    {
      String recipeText = ocr.performOCR(imageFile);
      HtmlCreator htmlCreator = new HtmlCreatorImpl.Builder().setRecipeText(recipeText)
          .build();

      List<String> htmlPage = htmlCreator.generateHtml();
      String htmlFilename = FileUtils.changePngExtensionToHtml(imageFile.getFileName()
          .toString());
      Files.createDirectories(Paths.get("output"));
      Files.write(Paths.get("output/", htmlFilename), htmlPage, StandardOpenOption.CREATE);
      
      System.out.println("HTML generated for " + htmlFilename);
    } catch (TesseractException e)
    {
      System.out.println("OCR failed");
      e.printStackTrace();
    } catch (IOException e)
    {
      System.out.println("HTML generation failed");
      e.printStackTrace();
    }
  }

}
