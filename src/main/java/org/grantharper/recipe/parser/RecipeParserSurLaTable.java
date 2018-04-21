package org.grantharper.recipe.parser;

public class RecipeParserSurLaTable extends RecipeParserAbstract
{

  private static final int YIELD_LENGTH = "Yield: ".length();
  private static final String FOOTER_IDENTIFIER = "www.sur";
  public static final int MAX_INGREDIENT_LINE_LENGTH = 90;
  public static final String BOOK_TITLE = "Sur La Table";

  private final String pageId;

  public RecipeParserSurLaTable(String fileName){
    pageId = getPageNumberFromFileName(fileName);
  }

  private String getPageNumberFromFileName(String fileName)
  {
    int periodPosition = fileName.indexOf(".");
    return fileName.substring(0,periodPosition);
  }
  
  @Override
  protected String extractServingSize()
  {
    if(this.servingSizeIndex == null) {
      throw new IllegalStateException("Cannot extract serving size before indices are set");
    }
    
    String fullServingSizeLine = recipeLines.get(this.servingSizeIndex);
    // the recipe text always says Yield: <serving size>
    return fullServingSizeLine.substring(YIELD_LENGTH, fullServingSizeLine.length());
  }
  
  protected void identifyLineIndexes()
  {

    //assumption is that the first line of the text is the title
    this.titleIndex = 0;
    this.servingSizeIndex = 1;

    boolean foundIngredientStart = false;
    boolean foundInstructionsStart = false;
    // start after the serving size line
    for (int lineIndex = this.servingSizeIndex + 1; lineIndex < recipeLines.size(); lineIndex++)
    {
      String line = recipeLines.get(lineIndex);

      if (!foundIngredientStart)
      {
        if (line.length() < MAX_INGREDIENT_LINE_LENGTH && !line.contains("."))
        {

          foundIngredientStart = true;
          this.ingredientsStartIndex = lineIndex;
        }
      } 
      else if(!foundInstructionsStart)
      {
        if (line.contains(".") || line.length() > MAX_INGREDIENT_LINE_LENGTH)
        {
          this.ingredientsEndIndex = lineIndex - 1;
          foundInstructionsStart = true;
          this.instructionsStartIndex = lineIndex;
          
        }
      }else {
        if(line.contains(FOOTER_IDENTIFIER)) {
          this.instructionsEndIndex = lineIndex - 1;
        }
      }

    }
    if(this.instructionsEndIndex == null){
      this.instructionsEndIndex = recipeLines.size() - 1;
    }
  }

  @Override
  protected String extractPageId()
  {
    return pageId;
  }

  @Override
  protected String extractBook()
  {
    return BOOK_TITLE;
  }

}
