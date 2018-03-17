package org.grantharper.recipe.ocr

import java.nio.file.Path

import static org.hamcrest.CoreMatchers.instanceOf

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import spock.lang.Specification

class RecipeCreatorSpec extends Specification {
  
  Path recipeTestFile
  List<String> recipeLines
  String recipeText
  RecipeCreator recipeCreator
  
  def setup() {
    recipeTestFile = Paths.get("src/test/resources/recipe.txt")
    recipeLines = Files.readAllLines(recipeTestFile, Charset.defaultCharset())
    recipeText = recipeLines.stream().reduce("", {String a, String b -> a + b + "\n"})
    recipeCreator = new RecipeCreator(recipeText)
  }
  
  def cleanup() {
    
  }

  def "Extract title from recipe raw text" () {
    
    when: "title is extracted from the recipe"
    String title = recipeCreator.extractTitle()
    
    then: "title is correct"
    title == "Whipped Feta with Grilled Pita Bread"
    
  }

  def "Extract serving size from recipe raw text" () {
    when: "serving size is extracted from the recipe"
    String servingSize = recipeCreator.extractServingSize()
    
    then: "title is correct"
    servingSize == "3 cups"
  }
  
  def "Identify indices of the ingredients, instructions, and footer" () {
    when: "indices are extracte from the recipe"
    recipeCreator.identifyIngredientAndInstructionsIndices()
    
    then: "they are correct"
    recipeCreator.footerIndex == 16
    recipeCreator.ingredientsStartIndex == 5
    recipeCreator.instructionsStartIndex == 12
  }

  def "Extract ingredient section from recipe raw text" () {
    given: "indices are set"
    recipeCreator.identifyIngredientAndInstructionsIndices()
    
    when: "ingredients are extracted from the recipe"
    List<String> ingredients = recipeCreator.extractIngredients()
    
    then: "ingredients are correct"
    ingredients.size() == 7
    ingredients.get(0) == "3 cups crumbled Greek feta"
    ingredients.get(6) == "Warm pita bread for serving"
    
    
  }

  def "Extract instructions section from recipe raw text" () {
    given: "indices are set"
    recipeCreator.identifyIngredientAndInstructionsIndices()
    
    when: "instructions are extracted from the recipe"
    String instructions = recipeCreator.extractInstructions()
    
    then: "instructions are correct"
    instructions != null
    !instructions.contains("www.surlatable.com")
    
    
  }
}
