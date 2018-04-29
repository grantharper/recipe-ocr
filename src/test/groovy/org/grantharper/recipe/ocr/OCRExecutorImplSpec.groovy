package org.grantharper.recipe.ocr

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import spock.lang.*
import java.nio.file.Paths;

class OCRExecutorImplSpec extends Specification
{

  OCRExecutor ocrExecutor;
  
  def "when Tesseract library fails, a runtime exception is thrown"() {
    given: "OCRExecutor with mocked Tesseract library"
    Tesseract tesseract = Stub(Tesseract)
    tesseract.doOCR(_,_) >> {throw new TesseractException()}
    tesseract.doOCR(_) >> {throw new TesseractException()}
    ocrExecutor = new OCRExecutorImpl(tesseract)
    
    when: "Tesseract failure"
    ocrExecutor.performTargetedOCR(Paths.get(""), null)
    
    then: "An OCR Exception is thrown"
    thrown OCRException
    
    
  }

}
