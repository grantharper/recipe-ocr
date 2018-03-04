package org.grantharper.recipe.ocr;

import java.io.File;
import java.nio.file.Path;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class OCRExecutorImpl implements OCRExecutor
{
  private static final String TESS_RESOURCES_FOLDER = "tessdata";
  private final Tesseract tesseract;

  public OCRExecutorImpl(Tesseract tesseract)
  {
    this.tesseract = tesseract;
    File tessDataFolder = LoadLibs.extractTessResources(TESS_RESOURCES_FOLDER);
    this.tesseract.setDatapath(tessDataFolder.getAbsolutePath());
  }

  public String performOCR(Path filePath)
  {
    try {
      return tesseract.doOCR(filePath.toFile());
    }catch(TesseractException e) {
      throw new OCRException(e);
    }
    
  }

}
