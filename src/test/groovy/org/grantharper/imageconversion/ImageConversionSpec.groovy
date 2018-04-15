package org.grantharper.imageconversion

import java.nio.file.Path
import java.nio.file.Paths

import spock.lang.Specification

class ImageConversionSpec extends Specification
{
  

  def "converts jpeg file into a png file" () {
    
    given: "image converter and jpeg file and output directory"
    ImageConverter imageConverter = new ImageConverter()
    Path jpegFile = Paths.get("src/test/resources/sample.jpg")
    Path outputDir = Paths.get("build")
    
    when: "image converter is executed"
    Path pngFile = imageConverter.convertJpegToPng(jpegFile, outputDir)
    
    
    then: "png file is created"
    pngFile != null
    pngFile.endsWith("build/sample.png")
    pngFile.toFile().exists()
    
  }
  
}
