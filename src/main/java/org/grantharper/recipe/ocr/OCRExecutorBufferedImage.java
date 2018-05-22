package org.grantharper.recipe.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class OCRExecutorBufferedImage implements OCRExecutor
{

  private static final String TESS_RESOURCES_FOLDER = "tessdata";
  private final Tesseract tesseract;

  public OCRExecutorBufferedImage(Tesseract tesseract)
  {
    this.tesseract = tesseract;
    File tessDataFolder = LoadLibs.extractTessResources(TESS_RESOURCES_FOLDER);
    this.tesseract.setDatapath(tessDataFolder.getAbsolutePath());
  }

  @Override
  public String performOCR(Path filePath) throws OCRException
  {
    try {
      BufferedImage bufferedImage = ImageIO.read(filePath.toFile());
      return this.tesseract.doOCR(bufferedImage);
    } catch (IOException e) {
      throw new OCRException(e);
    } catch (TesseractException e) {
      throw new OCRException(e);
    }
  }

  @Override
  public String performTargetedOCR(Path filePath, Rectangle targetedRectangle) throws OCRException
  {
    try {
      BufferedImage bufferedImage = ImageIO.read(filePath.toFile());
      return this.tesseract.doOCR(bufferedImage, targetedRectangle);
    } catch (IOException e) {
      throw new OCRException(e);
    } catch (TesseractException e) {
      throw new OCRException(e);
    }
  }
}
