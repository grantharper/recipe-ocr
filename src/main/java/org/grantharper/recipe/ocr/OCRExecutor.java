package org.grantharper.recipe.ocr;

import java.nio.file.Path;

public interface OCRExecutor
{

  public String performOCR(Path filePath) throws OCRException;
  
}
