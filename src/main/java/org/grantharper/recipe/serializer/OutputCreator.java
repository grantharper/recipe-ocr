package org.grantharper.recipe.serializer;

import org.grantharper.recipe.domain.Recipe;

public interface OutputCreator
{

  public String generateOutput(Recipe recipe);
  
}
