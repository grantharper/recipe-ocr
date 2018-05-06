package org.grantharper.recipe.userinterface;

import java.nio.file.Path;

public class RecipeMenuUserSelection
{

  private final RecipeMenuOption recipeMenuOption;
  private final Path sourcePath;
  private final Path destinationPath;

  private RecipeMenuUserSelection(RecipeMenuOption recipeMenuOption, Path sourcePath, Path destinationPath)
  {
    this.recipeMenuOption = recipeMenuOption;
    this.sourcePath = sourcePath;
    this.destinationPath = destinationPath;
  }

  public RecipeMenuOption getRecipeMenuOption()
  {
    return recipeMenuOption;
  }

  public Path getSourcePath()
  {
    return sourcePath;
  }

  public Path getDestinationPath()
  {
    return destinationPath;
  }

  static class RecipeMenuUserSelectionBuilder{
    private RecipeMenuOption recipeMenuOption;
    private Path sourcePath;
    private Path destinationPath;

    public RecipeMenuUserSelectionBuilder setRecipeMenuOption(RecipeMenuOption recipeMenuOption)
    {
      this.recipeMenuOption = recipeMenuOption;
      return this;
    }

    public RecipeMenuUserSelectionBuilder setSourcePath(Path sourcePath)
    {
      this.sourcePath = sourcePath;
      return this;
    }

    public RecipeMenuUserSelectionBuilder setDestinationPath(Path destinationPath)
    {
      this.destinationPath = destinationPath;
      return this;
    }

    public RecipeMenuUserSelection build()
    {
      return new RecipeMenuUserSelection(this.recipeMenuOption, this.sourcePath, this
              .destinationPath);
    }
  }
}
