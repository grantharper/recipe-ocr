package org.grantharper.recipe.converter

import net.sourceforge.tess4j.Tesseract
import org.grantharper.recipe.ocr.OCRExecutor
import org.grantharper.recipe.ocr.OCRExecutorImpl
import org.grantharper.recipe.ocr.RectangleCoordinateCsvParser
import org.grantharper.recipe.ocr.RectangleProvider
import spock.lang.Specification

import java.awt.Rectangle
import java.nio.file.Path
import java.nio.file.Paths

class PngToTextConverterSpec extends Specification
{

  Path inputImage;
  OCRExecutor ocrExecutor;
  PngToTextConverter pngToTextConverter;

  def setup()
  {
    ocrExecutor = new OCRExecutorImpl(new Tesseract())
    inputImage = Paths.get("src/test/resources/sample.png")
  }

  def cleanup()
  {

  }


  def "pull in text from multiple rectangles"()
  {
    given: "image and rectangle regions"
    List<Rectangle> rectangleList = new ArrayList<>()
    RectangleProvider rectangleProvider = Stub(RectangleCoordinateCsvParser.class)
    Rectangle rectangle1 = new Rectangle(10, 0, 960, 300)
    Rectangle rectangle2 = new Rectangle(10, 300, 960, 300)
    rectangleList.add(rectangle1)
    rectangleList.add(rectangle2)
    rectangleProvider.getRectangles() >> rectangleList
    pngToTextConverter = new PngToTextConverter(ocrExecutor, rectangleProvider)

    when: "ocr is performed"
    List<String> extractedText = pngToTextConverter.extractTextFromRectangles(inputImage, rectangleList)

    then: "text is extracted"
    extractedText.get(0).startsWith("sample")
    extractedText.get(1).contains("Page 450")
    extractedText.get(2).startsWith("Salt")

  }

}
