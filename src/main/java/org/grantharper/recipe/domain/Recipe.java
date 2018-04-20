package org.grantharper.recipe.domain;

import java.util.List;

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
