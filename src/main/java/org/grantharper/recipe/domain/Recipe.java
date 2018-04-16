package org.grantharper.recipe.domain;

import java.util.List;

public class Recipe
{

  private final String book;
  private final String title;
  private final String pageId;
  private final String servingSize;
  private final List<String> ingredients;
  private final String instructions;

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


  @Override
  public String toString()
  {
    return "Recipe [title=" + title + "]";
  }


}
