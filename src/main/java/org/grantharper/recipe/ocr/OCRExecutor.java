package org.grantharper.recipe.ocr;

import java.awt.*;
import java.nio.file.Path;

public interface OCRExecutor
{

  public String performOCR(Path filePath) throws OCRException;

  public String performTargetedOCR(Path filePath, Rectangle targetedRectangle) throws OCRException;
  
}
