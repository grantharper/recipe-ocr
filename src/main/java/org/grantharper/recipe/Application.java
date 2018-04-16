package org.grantharper.recipe;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.grantharper.imageconversion.ImageConverter;
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

  private OCRExecutor ocr = new OCRExecutorImpl(new Tesseract());
  private OutputCreator outputCreator = new RecipeJsonCreator();
  private ImageConverter imageConverter = new ImageConverter();

  private final String pngOutputDir = "data/output/png";
  private final String jsonOutputDir = "data/output/json";
  private final String jpgInputDir = "data/input";
  private final String textOutputDir = "data/output/text";
  private final String htmlOutputDir = "data/output/html";

  public static void main(String[] args)
  {
    System.out.println("OCR App Running");
    Application app = new Application();
    app.executeOCRLoop();

  }

  private void executeOCRLoop(){
    try
    {
      Files.createDirectories(Paths.get(pngOutputDir));
      Files.createDirectories(Paths.get(jsonOutputDir));
      Files.createDirectories(Paths.get(textOutputDir));
      Files.list(Paths.get(jpgInputDir))
              .filter(p -> p.getFileName()
                      .toString()
                      .endsWith(".jpg"))
              .forEach(this::performOCR);
    } catch (IOException e)
    {
      System.out.println("Input/Output directory problem");
      e.printStackTrace();
    }

  }



  void performOCR(Path imageFile)
  {

    try
    {

      Path pngImageFile = imageConverter.convertJpegToPng(imageFile, Paths.get(pngOutputDir));
      String recipeText = getTextFromImage(pngImageFile);

      Recipe recipe = new RecipeParserSurLaTable(pngImageFile.getFileName().toString()).parse(recipeText);
      String json = outputCreator.generateOutput(recipe);
      List<String> output = Arrays.asList(json);
      
      Files.write(Paths.get(jsonOutputDir, FileUtils.changePngExtensionToJson(imageFile.getFileName()
          .toString())), output, Charset.defaultCharset(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (OCRException e)
    {
      System.out.println("Error with OCR processing: " + imageFile);
      e.printStackTrace();
    } catch (IOException e)
    {
      System.out.println("Json file generation failed");
      e.printStackTrace();
    }
  }

  String getTextFromImage(Path pngImageFile) throws IOException{
    String recipeText;
    Path textRepresentationFilePath = Paths.get(textOutputDir, FileUtils.changePngExtensionToTxt(pngImageFile.getFileName().toString()));
    if(textRepresentationFilePath.toFile().exists()){
      recipeText = Files.readAllLines(textRepresentationFilePath)
              .stream().collect(Collectors.joining("\n"));
    }else{
      recipeText = ocr.performOCR(pngImageFile);
      writeOcrResultToTextFile(recipeText, pngImageFile);
    }

    return recipeText;
  }

  void writeOcrResultToTextFile(String recipeText, Path imageFile) throws IOException
  {
    Files.write(Paths.get(textOutputDir, FileUtils.changePngExtensionToTxt(imageFile.getFileName()
        .toString())), Arrays.asList(recipeText), Charset.defaultCharset(), StandardOpenOption.CREATE);
  }

  void writeOcrResultToHtml(String recipeText, Path imageFile) throws IOException
  {
    HtmlCreator htmlCreator = new HtmlCreatorImpl.Builder().setRecipeText(recipeText)
        .build();

    List<String> htmlPage = htmlCreator.generateHtmlPage();
    String htmlFilename = FileUtils.changePngExtensionToHtml(imageFile.getFileName()
        .toString());

    Files.write(Paths.get(htmlOutputDir, htmlFilename), htmlPage, StandardOpenOption.CREATE);
  }

}
