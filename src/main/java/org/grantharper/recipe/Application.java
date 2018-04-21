package org.grantharper.recipe;

import net.sourceforge.tess4j.Tesseract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.imageconversion.ImageConverter;
import org.grantharper.recipe.domain.Recipe;
import org.grantharper.recipe.ocr.OCRException;
import org.grantharper.recipe.ocr.OCRExecutor;
import org.grantharper.recipe.ocr.OCRExecutorImpl;
import org.grantharper.recipe.parser.RecipeParserSurLaTable;
import org.grantharper.recipe.serializer.*;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application
{

  private static final Logger logger = LogManager.getLogger(Application.class);

  private OCRExecutor ocr = new OCRExecutorImpl(new Tesseract());
  private OutputCreator outputCreator = new RecipeJsonCreator();
  private ImageConverter imageConverter = new ImageConverter();

  public static final String pngOutputDir = "data/output/png";
  public static final String jsonOutputDir = "data/output/json";
  public static final String jpgInputDir = "data/input";
  public static final String textOutputDir = "data/output/text";
  public static final String htmlOutputDir = "data/output/html";

  private final Rectangle originalRecipeRectangle = new Rectangle(150, 475, 2250, 2670);
  private final Rectangle updatedRecipeRectangle = new Rectangle(150, 400, 2250, 2770);



  public static void main(String[] args)
  {
    logger.info("OCR App Running");
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
      logger.error("Input/Output directory problem", e);
    }

  }



  void performOCR(Path imageFile)
  {

    try
    {
      logger.info("processing " + imageFile.getFileName().toString());
      Path pngImageFile = imageConverter.convertJpegToPng(imageFile, Paths.get(pngOutputDir));
      String recipeText = getTextFromImage(pngImageFile);

      Recipe recipe = new RecipeParserSurLaTable(pngImageFile.getFileName().toString()).parse(recipeText);
      String json = outputCreator.generateOutput(recipe);
      List<String> output = Arrays.asList(json);
      
      Files.write(Paths.get(jsonOutputDir, FileUtils.changePngExtensionToJson(imageFile.getFileName()
          .toString())), output, Charset.defaultCharset(),
              StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (OCRException e)
    {
      logger.error("Error with OCR processing: " + imageFile, e);
    } catch (IOException e)
    {
      logger.error("Json file generation failed", e);
    }
  }

  String getTextFromImage(Path pngImageFile) throws IOException{
    String recipeText;
    Path textRepresentationFilePath = Paths.get(textOutputDir,
            FileUtils.changePngExtensionToTxt(pngImageFile.getFileName().toString()));

    if(textRepresentationFilePath.toFile().exists()){
      recipeText = Files.readAllLines(textRepresentationFilePath)
              .stream().collect(Collectors.joining("\n"));
    }else{
      recipeText = ocr.performTargetedOCR(pngImageFile, updatedRecipeRectangle);
      writeOcrResultToTextFile(recipeText, pngImageFile);
    }

    return recipeText;
  }



  void writeOcrResultToTextFile(String recipeText, Path imageFile) throws IOException
  {
    Files.write(Paths.get(textOutputDir, FileUtils.changePngExtensionToTxt(imageFile.getFileName()
        .toString())), Arrays.asList(recipeText), Charset.defaultCharset(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  void writeOcrResultToHtml(String recipeText, Path imageFile) throws IOException
  {
    HtmlCreator htmlCreator = new HtmlCreatorImpl.Builder().setRecipeText(recipeText)
        .build();

    List<String> htmlPage = htmlCreator.generateHtmlPage();
    String htmlFilename = FileUtils.changePngExtensionToHtml(imageFile.getFileName()
        .toString());

    Files.write(Paths.get(htmlOutputDir, htmlFilename), htmlPage, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

}
