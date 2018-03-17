package org.grantharper.recipe.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecipeJsonCreator implements OutputCreator
{
  
  private ObjectMapper mapper = new ObjectMapper();
  

  @Override
  public String generateOutput(Recipe recipe)
  {
    try
    {
      return mapper.writeValueAsString(recipe);
    } catch (JsonProcessingException e)
    {
      e.printStackTrace();
      return "{\"error\": \"JsonProcessingException\"}";
    }
  }

}
