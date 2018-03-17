package org.grantharper.recipe.ocr;

import java.util.List;

public class Recipe
{

  private String title;
  private String servingSize;
  private List<String> ingredients;
  private String instructions;
  
  
  
  public String getTitle()
  {
    return title;
  }
  public String getServingSize()
  {
    return servingSize;
  }

  public List<String> getIngredients()
  {
    return ingredients;
  }
  public String getInstructions()
  {
    return instructions;
  }
  
  
  

  public Recipe(String title, String servingSize, List<String> ingredients, String instructions)
  {
    super();
    this.title = title;
    this.servingSize = servingSize;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }
  
  
  @Override
  public String toString()
  {
    return "Recipe [title=" + title + "]";
  }
  
  
  
  
}
