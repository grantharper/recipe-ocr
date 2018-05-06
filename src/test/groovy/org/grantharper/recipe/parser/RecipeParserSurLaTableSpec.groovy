package org.grantharper.recipe.parser

import java.nio.file.Path
import java.util.stream.Collectors

import static org.hamcrest.CoreMatchers.instanceOf

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.grantharper.recipe.parser.RecipeParserAbstract
import org.grantharper.recipe.parser.RecipeParserSurLaTable

import spock.lang.Specification

class RecipeParserSurLaTableSpec extends Specification
{

  Path recipeTestFile
  List<String> recipeLines
  String recipeText
  RecipeParserAbstract recipeCreator

  def setup()
  {
    recipeTestFile = Paths.get("src/test/resources/sample-recipe.txt")
    recipeLines = Files.readAllLines(recipeTestFile, Charset.defaultCharset())
    recipeText = recipeLines.stream().collect(Collectors.joining("\n"));
    recipeCreator = new RecipeParserSurLaTable();
    recipeCreator.parseRecipeLines(recipeText);
  }

  def cleanup()
  {

  }

  def "Extract title from recipe raw text"()
  {
    given: "indices are set"
    recipeCreator.identifyLineIndexes()

    when: "title is extracted from the recipe"
    String title = recipeCreator.extractTitle()

    then: "title is correct"
    title == "Whipped Feta with Grilled Pita Bread"

  }

  def "Extract pageId from recipe filename"()
  {
    when: "indices are set"
    recipeCreator.identifyLineIndexes()

    then: "recipe parser instantiation provides pageId"
    recipeCreator.extractPageId() == "102"

  }

  def "Extract book title from recipe"()
  {
    expect: "recipe sur la table parser declares tilte"
    recipeCreator.extractBook() == RecipeParserSurLaTable.BOOK_TITLE
  }

  def "Extract serving size from recipe raw text"()
  {
    given: "indices are set"
    recipeCreator.identifyLineIndexes()

    when: "serving size is extracted from the recipe"
    String servingSize = recipeCreator.extractServingSize()

    then: "title is correct"
    servingSize == "3 cups"
  }

  def "Identify indices of the ingredients, instructions, and footer"()
  {
    when: "indices are extracte from the recipe"
    recipeCreator.recipeLines
    recipeCreator.identifyLineIndexes()

    then: "they are correct"
    recipeCreator.instructionsEndIndex == 16
    recipeCreator.ingredientsStartIndex == 6
    recipeCreator.ingredientsEndIndex == 12
    recipeCreator.instructionsStartIndex == 13
  }

  def "Extract ingredient section from recipe raw text"()
  {
    given: "indices are set"
    recipeCreator.identifyLineIndexes()

    when: "ingredients are extracted from the recipe"
    List<String> ingredients = recipeCreator.extractIngredients()

    then: "ingredients are correct"
    ingredients.size() == 7
    ingredients.get(0) == "3 cups crumbled Greek feta"
    ingredients.get(6) == "Warm pita bread for serving"


  }

  def "Extract instructions section from recipe raw text"()
  {
    given: "indices are set"
    recipeCreator.identifyLineIndexes()

    when: "instructions are extracted from the recipe"
    String instructions = recipeCreator.extractInstructions()

    then: "instructions are correct"
    instructions != null
    !instructions.contains("www.surlatable.com")


  }

}
