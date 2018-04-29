package org.grantharper.recipe.parser;

import java.util.List;

public interface RecipeParserExtended extends RecipeParser
{

  String extractBook();

  String extractPageId();

  String extractTitle();

  String extractServingSize();

  List<String> extractIngredients();

  String extractInstructions();

}
