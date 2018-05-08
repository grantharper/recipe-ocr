package org.grantharper.recipe.parser;

import org.grantharper.recipe.domain.Recipe;

public interface RecipeParser
{
  Recipe parse(String text);
}
