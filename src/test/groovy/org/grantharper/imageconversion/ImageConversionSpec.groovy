package org.grantharper.imageconversion

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Files
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
    Files.createDirectories(Paths.get("build"))

    when: "image converter is executed"
    Path pngFile = imageConverter.convertJpegToPng(jpegFile, outputDir)
    
    
    then: "png file is created"
    pngFile != null
    pngFile.endsWith("build/sample.png")
    pngFile.toFile().exists()
    
  }

  def "test imageIO"() {
    given: "input and output file paths"
    File inputFilePath = new File("src/test/resources/sample.jpg")
    File outputFilePath = new File("src/test/out/sample.png")
    Files.createDirectories(Paths.get("src/test/out"))

    when: "image io is used to load image"
    try {
      BufferedImage bufferedImage = ImageIO.read(inputFilePath);
      ImageIO.write(bufferedImage, "png", outputFilePath)
    } catch (IOException e) {
      e.printStackTrace();
    }

    then: "it works"
    outputFilePath.exists()

  }
  
}
