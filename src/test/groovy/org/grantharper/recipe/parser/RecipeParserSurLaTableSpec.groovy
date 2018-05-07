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
    recipeTestFile = Paths.get("src/test/resources/sample.txt")
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
    title == "Best Pizza"

  }

  def "Extract pageId from recipe filename"()
  {
    when: "indices are set"
    recipeCreator.identifyLineIndexes()

    then: "recipe parser instantiation provides pageId"
    recipeCreator.extractPageId() == "450"

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

    then: "serving size is correct"
    servingSize == "2 large pizzas"
  }

  def "Identify indices of the ingredients, instructions, and footer"()
  {
    when: "indices are extracte from the recipe"
    recipeCreator.recipeLines
    recipeCreator.identifyLineIndexes()

    then: "they are correct"
    recipeCreator.ingredientsStartIndex == 3
    recipeCreator.ingredientsEndIndex == 8
    recipeCreator.instructionsStartIndex == 9
    recipeCreator.instructionsEndIndex == 11
  }

  def "Extract ingredient section from recipe raw text"()
  {
    given: "indices are set"
    recipeCreator.identifyLineIndexes()

    when: "ingredients are extracted from the recipe"
    List<String> ingredients = recipeCreator.extractIngredients()

    then: "ingredients are correct"
    ingredients.size() == 6
    ingredients.get(0) == "Olive oil"
    ingredients.get(5) == "Cheese"


  }

  def "Extract instructions section from recipe raw text"()
  {
    given: "indices are set"
    recipeCreator.identifyLineIndexes()

    when: "instructions are extracted from the recipe"
    String instructions = recipeCreator.extractInstructions()

    then: "instructions are correct"
    instructions != null
    !instructions.contains("Mix together flour and salt")


  }

}
