package com.example.recipe;

import org.grantharper.recipe.parser.RecipeParser;
import org.grantharper.recipe.parser.RecipeParserAbstract;

public class AwesomeRecipeParser extends RecipeParserAbstract implements RecipeParser
{
  @Override
  protected void identifyLineIndexes()
  {
    this.pageIdIndex = 1;
    this.servingSizeIndex = 3;
    this.titleIndex = 2;
    this.ingredientsStartIndex = 4;
    this.ingredientsEndIndex = 10;
    this.instructionsStartIndex = 12;
    this.instructionsEndIndex = recipeLines.size() - 1;
  }

  @Override
  public String extractBook()
  {
    return "Awesome Recipe Book";
  }
}
