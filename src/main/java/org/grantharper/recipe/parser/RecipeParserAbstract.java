package org.grantharper.recipe.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.grantharper.recipe.domain.Recipe;

public abstract class RecipeParserAbstract implements RecipeParser
{

  protected List<String> recipeLines;
  protected Integer titleIndex;
  protected Integer servingSizeIndex;
  protected Integer ingredientsStartIndex;
  protected Integer ingredientsEndIndex;
  protected Integer instructionsStartIndex;
  protected Integer instructionsEndIndex;
  
  /*
   * Subclasses provide instructions on how to identify indices so 
   * that the recipe information may be extracted from various
   * formats
   */
  protected abstract void identifyLineIndexes();
  
  protected void parseRecipeLines(String text) {
    String[] output = text.split("\n");
    this.recipeLines = Arrays.asList(output)
        .stream()
        .filter(s -> !s.trim()
            .equals(""))
        .collect(Collectors.toList());
  }
  
  @Override
  public final Recipe parse(String text)
  {
    parseRecipeLines(text);
    identifyLineIndexes();
    String title = extractTitle();
    String servingSize = extractServingSize();
    List<String> ingredients = extractIngredients();
    String instructions = extractInstructions();
    return new Recipe(title, servingSize, ingredients, instructions);
  }

  protected String extractTitle()
  {
    if(this.titleIndex == null) {
      throw new IllegalStateException("Cannot extract title before indices are set");
    }
    return recipeLines.get(this.titleIndex);
  }  

  protected String extractServingSize()
  {
    if(this.servingSizeIndex == null) {
      throw new IllegalStateException("Cannot extract serving size before indices are set");
    }
    return recipeLines.get(this.servingSizeIndex);
  }

  protected List<String> extractIngredients()
  {
    if(this.ingredientsStartIndex == null || this.ingredientsEndIndex == null) {
      throw new IllegalStateException("Cannot extract ingredients before indices are set");
    }
    List<String> ingredients = new ArrayList<>();

    for (int i = this.ingredientsStartIndex; i <= this.ingredientsEndIndex; i++)
    {
      ingredients.add(this.recipeLines.get(i));
    }

    return ingredients;
  }

  protected String extractInstructions()
  {
    if(this.instructionsStartIndex == null || this.instructionsEndIndex == null) {
      throw new IllegalStateException("Cannot extract instructions before indices are set");
    }
    String instructions = "";

    for (int i = this.instructionsStartIndex; i <= this.instructionsEndIndex; i++)
    {
      instructions += this.recipeLines.get(i) + " ";
    }
    return instructions;
  }

}
