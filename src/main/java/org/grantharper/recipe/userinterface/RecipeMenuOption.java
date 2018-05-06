package org.grantharper.recipe.userinterface;

public enum RecipeMenuOption
{
  PROCESS_IMAGE_TO_TEXT(1),
  PROCESS_TEXT_TO_JSON(2),
  PROCESS_JSON_TO_ELASTICSEARCH(3);


  private int index;

  RecipeMenuOption(int index)
  {
    this.index = index;
  }

  public static RecipeMenuOption getOptionFromIndex(int index)
  {
    if (index == PROCESS_IMAGE_TO_TEXT.index) {
      return PROCESS_IMAGE_TO_TEXT;
    } else if (index == PROCESS_TEXT_TO_JSON.index) {
      return PROCESS_TEXT_TO_JSON;
    } else if (index == PROCESS_JSON_TO_ELASTICSEARCH.index) {
      return PROCESS_JSON_TO_ELASTICSEARCH;
    } else {
      throw new RuntimeException("invalid index for enum selection");
    }

  }
  //use Enums to define the two menus
  //use the builder pattern to make this class immutable and
  //be created by the RecipeProcessingMenu class and then passed to RecipeApplication
}
