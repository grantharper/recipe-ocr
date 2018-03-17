package org.grantharper.recipe.ocr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeCreator
{

  private List<String> recipeLines;
  private int ingredientsStartIndex;
  private int instructionsStartIndex;
  private int footerIndex;

  public RecipeCreator(String recipeText)
  {
    String[] output = recipeText.split("\n");
    recipeLines = Arrays.asList(output)
        .stream()
        .filter(s -> !s.trim()
            .equals(""))
        .collect(Collectors.toList());
  }

  public Recipe convertTextToRecipe()
  {
    identifyIngredientAndInstructionsIndices();
    String title = extractTitle();
    String servingSize = extractServingSize();
    List<String> ingredients = extractIngredients();
    String instructions = extractInstructions();
    return new Recipe(title, servingSize, ingredients, instructions);
  }

  private static final int TITLE_LINE_INDEX = 0;

  protected String extractTitle()
  {
    // assumption is that the first line of the text is the title
    return recipeLines.get(TITLE_LINE_INDEX);
  }

  private static final int SERVICE_SIZE_LINE_INDEX = 1;
  private static final int YIELD_LENGTH = "Yield: ".length();

  protected String extractServingSize()
  {
    String fullServingSizeLine = recipeLines.get(SERVICE_SIZE_LINE_INDEX);
    // the recipe text always says Yield: <serving size>
    return fullServingSizeLine.substring(YIELD_LENGTH, fullServingSizeLine.length());
  }

  protected List<String> extractIngredients()
  {
    if(this.ingredientsStartIndex == 0 || this.instructionsStartIndex == 0) {
      throw new IllegalStateException("Cannot extract ingredients before indices are set");
    }
    List<String> ingredients = new ArrayList<>();

    for (int i = this.ingredientsStartIndex; i < this.instructionsStartIndex; i++)
    {
      ingredients.add(recipeLines.get(i));
    }

    return ingredients;
  }
  
  private static final String FOOTER_IDENTIFIER = "www.surla";
  public static final int MAX_INGREDIENT_LINE_LENGTH = 55;

  protected void identifyIngredientAndInstructionsIndices()
  {

    boolean foundIngredientStart = false;
    boolean foundInstructionsStart = false;
    // start after the serving size line
    for (int lineIndex = SERVICE_SIZE_LINE_INDEX + 1; lineIndex < recipeLines.size(); lineIndex++)
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
        if (line.length() > MAX_INGREDIENT_LINE_LENGTH)
        {
          foundInstructionsStart = true;
          this.instructionsStartIndex = lineIndex;
        }
      }else {
        if(line.contains(FOOTER_IDENTIFIER)) {
          this.footerIndex = lineIndex;
        }
      }

    }
  }

  protected String extractInstructions()
  {
    if(this.instructionsStartIndex == 0 || this.footerIndex == 0) {
      throw new IllegalStateException("Cannot extract instructions before indices are set");
    }
    String instructions = "";

    for (int i = this.instructionsStartIndex; i < this.footerIndex; i++)
    {
      instructions += recipeLines.get(i) + " ";
    }
    return instructions;
  }

}
