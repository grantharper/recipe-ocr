package org.grantharper.recipe.userinterface;

public enum RecipeMenuOption
{
  PROCESS_IMAGE_TO_TEXT("1", "Process image into text", "data/input"),
  PROCESS_TEXT_TO_JSON("2", "Process text into JSON", "data/output/text"),
  PROCESS_JSON_TO_ELASTICSEARCH("3", "Load JSON to the Elasticsearch Index", "data/output/json"),
  EXIT("4", "Exit the application", "nothing");


  private String optionNumber;
  private String menuListing;
  private String defaultSourceDirectory;

  RecipeMenuOption(String optionNumber, String menuListing, String defaultSourceDirectory)
  {
    this.optionNumber = optionNumber;
    this.menuListing = menuListing;
    this.defaultSourceDirectory = defaultSourceDirectory;
  }

  public String getOptionNumber()
  {
    return optionNumber;
  }

  public String getMenuListing()
  {
    return menuListing;
  }

  public String getDefaultSourceDirectory()
  {
    return defaultSourceDirectory;
  }

  public static RecipeMenuOption getOptionFromMenuOption(String menuOption)
  {
    if (menuOption == null) {
      throw new RuntimeException("No console input received");
    } else if (menuOption.equals(PROCESS_IMAGE_TO_TEXT.optionNumber)) {
      return PROCESS_IMAGE_TO_TEXT;
    } else if (menuOption.equals(PROCESS_TEXT_TO_JSON.optionNumber)) {
      return PROCESS_TEXT_TO_JSON;
    } else if (menuOption.equals(PROCESS_JSON_TO_ELASTICSEARCH.optionNumber)) {
      return PROCESS_JSON_TO_ELASTICSEARCH;
    } else if (menuOption.equals(EXIT.optionNumber)) {
      return EXIT;
    } else {
      return null;
    }

  }

}
