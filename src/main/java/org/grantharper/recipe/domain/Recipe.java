package org.grantharper.recipe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class Recipe
{

  private String book;
  private String title;
  private String pageId;
  private String servingSize;
  private List<String> ingredients;
  private String instructions;

  public String getBook()
  {
    return book;
  }

  public String getTitle()
  {
    return title;
  }

  public String getServingSize()
  {
    return servingSize;
  }

  public String getPageId()
  {
    return pageId;
  }

  public List<String> getIngredients()
  {
    return ingredients;
  }

  public String getInstructions()
  {
    return instructions;
  }

  @JsonProperty
  public String getIngredientsList()
  {
    return ingredients.stream().collect(Collectors.joining("\n"));
  }

  @JsonIgnore //this is to allow for creating the POJO from json that contains the ingredientsList field
  public void setIngredientsList(String ingredientsList)
  {
    //do nothing since all ingredient information is in the ingredients variable
  }

  public Recipe(String book, String title, String pageId, String servingSize, List<String> ingredients, String instructions)
  {
    super();
    this.book = book;
    this.title = title;
    this.pageId = pageId;
    this.servingSize = servingSize;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }

  public Recipe(){}

  @Override
  public String toString()
  {
    return "Recipe [title=" + title + "]";
  }

}
