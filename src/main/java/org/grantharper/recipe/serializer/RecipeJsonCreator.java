package org.grantharper.recipe.serializer;

import org.grantharper.recipe.domain.Recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecipeJsonCreator implements OutputCreator
{

  private final ObjectMapper mapper;

  @Autowired
  public RecipeJsonCreator(ObjectMapper objectMapper)
  {
    this.mapper = objectMapper;
  }

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
