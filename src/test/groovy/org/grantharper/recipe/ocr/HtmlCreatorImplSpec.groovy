package org.grantharper.recipe.ocr

import org.grantharper.recipe.Application
import org.grantharper.recipe.ocr.HtmlCreator
import org.grantharper.recipe.ocr.HtmlCreatorImpl

class HtmlCreatorImplSpec extends spock.lang.Specification {

  def multiLineString = '''
                        Title
                        2/3 cup extra—virgin olive oil, plus more for serving 
                        Zest and juice of 1 lemon
                        Freshly ground black pepper
                        1 teaspoon zaatar
                        Pitted Kalamata olives for serving
                        '''
  HtmlCreatorImpl htmlCreatorImpl = new HtmlCreatorImpl.Builder().setRecipeText(multiLineString).build()
  HtmlCreator htmlCreator = new HtmlCreatorImpl.Builder().setRecipeText(multiLineString).build()


  def "paragraph tag surround"() {
    given: "application class and input string"
    String input = "Take me to the river"

    expect: "input is surrounded with paragraph tags"
    htmlCreatorImpl.surroundLineBreaksWithParagraphs(input) == "<p>" + input + "</p>"
  }

  def "create multi-line paragraph" () {
    given: "multi-line string"
    def input = multiLineString

    when: "multi line string is processed into paragraphs"
    List<String> output = htmlCreatorImpl.createMultiLineParagraphs(input)

    then: "output has paragraphs surrounding it"
    output.size() == 6
    output.get(0).startsWith("<p>")
    output.get(0).endsWith("</p>")
  }

  def "create html5 document" () {
    given: "input string"
    def input = ["blahblah"]

    when: "html5 is needed"
    List<String> output = htmlCreatorImpl.surroundWithHtml5Document(input)

    then: "output is valid html5"
    output.get(0) == "<!DOCTYPE html>"
    output.get(output.size() - 1) == "</html>"
  }

  def "div for recipe title" (){
    given: "recipe title"
    def title = "Grilled Cheese"

    when: "recipe title is needed"
    String output = htmlCreatorImpl.surroundTitleWithDivs(title)

    then: "a div is created"
    output == "<div class='ocr-title'>" + title + "</div>"
  }

  def "divs for other recipe information" () {
    given: "recipe info"
    def recipe = "3/4 cup cheddar"

    when: "recipe is processed"
    String output = htmlCreatorImpl.surroundStandardLineBreaksWithDivs(recipe)

    then: "div is created"
    output == "<div class='ocr-recipe'>" + recipe + "</div>"
  }

  def "create divs" () {
    when: "divs are required"
    List<String> output = htmlCreatorImpl.generateDivs()

    then: "output has the title at the top"
    output.get(0) == "<div class='ocr-title'>Title</div>"
    output.get(1) == "<div class='ocr-recipe'>2/3 cup extra—virgin olive oil, plus more for serving</div>"
  }
}