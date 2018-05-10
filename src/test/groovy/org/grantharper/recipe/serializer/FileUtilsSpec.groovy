package org.grantharper.recipe.serializer

import spock.lang.*

class FileUtilsSpec extends Specification
{

  def "replaces png extension with html extension"()
  {
    given: "png filename"
    String pngFilename = "sample.png"

    when: "conversion to html filename is called"
    String htmlFilename = FileUtils.changeFileExtensionToHtml(pngFilename)

    then: "html filename is created"
    htmlFilename == "sample.html"

  }

  def "remove extension"()
  {
    given: "filename"
    String filename = "blah.txt"

    when: "extension is removed"
    String withoutExtension = FileUtils.removeExtension(filename)

    then: "only file name remains"
    withoutExtension == "blah"
  }

  @Unroll
  def "replace any extension"()
  {
    when: "filename is provided"
    String updatedFilename = FileUtils.changeExtension(filename, updatedExtension)

    then: "extension is changed"
    updatedFilename == expected

    where: "sample data"
    filename        | updatedExtension      | expected
    "joke.png"      | ".txt"                | "joke.txt"
    "sample.jpeg"   | ".js"                 | "sample.js"
    "cool.cool.txt" | ".png"                | "cool.cool.png"

  }


}
