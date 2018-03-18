package org.grantharper.recipe;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.grantharper.recipe.domain.Recipe;
import org.grantharper.recipe.ocr.OCRException;
import org.grantharper.recipe.ocr.OCRExecutor;
import org.grantharper.recipe.ocr.OCRExecutorImpl;
import org.grantharper.recipe.parser.RecipeParserAbstract;
import org.grantharper.recipe.parser.RecipeParserSurLaTable;
import org.grantharper.recipe.serializer.FileUtils;
import org.grantharper.recipe.serializer.HtmlCreator;
import org.grantharper.recipe.serializer.HtmlCreatorImpl;
import org.grantharper.recipe.serializer.OutputCreator;
import org.grantharper.recipe.serializer.RecipeJsonCreator;

import net.sourceforge.tess4j.Tesseract;

public class Application
{

  OCRExecutor ocr = new OCRExecutorImpl(new Tesseract());

  public static void main(String[] args)
  {
    System.out.println("OCR App Running");
    Application app = new Application();

    try
    {
      Files.createDirectories(Paths.get("output"));
      Files.list(Paths.get("img/"))
          .filter(p -> p.getFileName()
              .toString()
              .endsWith(".png"))
          .forEach(app::performOCR);
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
      
      String json;
      OutputCreator outputCreator = new RecipeJsonCreator();
      Recipe recipe = new RecipeParserSurLaTable().parse(recipeText);
      json = outputCreator.generateOutput(recipe);
      List<String> output = Arrays.asList(json);
      
      Files.write(Paths.get("output/", FileUtils.changePngExtensionToJson(imageFile.getFileName()
          .toString())), output, Charset.defaultCharset(), StandardOpenOption.CREATE);

    } catch (OCRException e)
    {
      System.out.println("Error with OCR processing: " + imageFile);
      e.printStackTrace();
    } catch (IOException e)
    {
      System.out.println("HTML generation failed");
      e.printStackTrace();
    }
  }

  public void writeOcrResultToTextFile(String recipeText, Path imageFile) throws IOException
  {
    Files.write(Paths.get("output/", FileUtils.changePngExtensionToTxt(imageFile.getFileName()
        .toString())), Arrays.asList(recipeText), Charset.defaultCharset(), StandardOpenOption.CREATE);
  }

  public void writeOcrResultToHtml(String recipeText, Path imageFile) throws IOException
  {
    HtmlCreator htmlCreator = new HtmlCreatorImpl.Builder().setRecipeText(recipeText)
        .build();

    List<String> htmlPage = htmlCreator.generateHtmlPage();
    String htmlFilename = FileUtils.changePngExtensionToHtml(imageFile.getFileName()
        .toString());

    Files.write(Paths.get("output/", htmlFilename), htmlPage, StandardOpenOption.CREATE);
  }

}
