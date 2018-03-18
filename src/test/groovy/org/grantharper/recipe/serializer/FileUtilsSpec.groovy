package org.grantharper.recipe.serializer

import org.grantharper.recipe.serializer.FileUtils

import spock.lang.*

class FileUtilsSpec extends Specification
{

  def "replaces png extension with html extension" () {
    given: "png filename"
    String pngFilename = "sample.png"
    
    when: "conversion to html filename is called"
    String htmlFilename = FileUtils.changePngExtensionToHtml(pngFilename)
    
    then: "html filename is created"
    htmlFilename == "sample.html"
    
  }
  
}
