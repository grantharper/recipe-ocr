package org.grantharper.recipe.ocr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlCreatorImpl implements HtmlCreator
{

  private final String recipeText;

  private HtmlCreatorImpl(String recipeText)
  {
    this.recipeText = recipeText;
  }

  public static class Builder
  {
    private String recipeText;

    public Builder setRecipeText(String recipeText)
    {
      this.recipeText = recipeText;
      return this;
    }

    public HtmlCreator build()
    {
      return new HtmlCreatorImpl(this.recipeText);
    }
  }

  public List<String> generateHtml()
  {
    return surroundWithHtml5Document(createMultiLineParagraphs(this.recipeText));
  }
  
  protected List<String> surroundWithHtml5Document(List<String> input) 
  {
    try
    {
      List<String> header = Files.lines(Paths.get("html/top.html")).collect(Collectors.toList());
      List<String> footer = Files.lines(Paths.get("html/bottom.html")).collect(Collectors.toList());
      List<String> html = new ArrayList<>();
      html.addAll(header);
      html.addAll(input);
      html.addAll(footer);
      return html;
    } catch (IOException e)
    {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
  
  protected List<String> createMultiLineParagraphs(String input)
  {
    String[] output = this.recipeText.split("\n");
    return Arrays.asList(output)
        .stream()
        .filter(s -> !s.trim()
            .equals(""))
        .map(s -> surroundLineBreaksWithParagraphs(s.trim()))
        .collect(Collectors.toList());
  }

  protected String surroundLineBreaksWithParagraphs(String input)
  {
    String output = "<p>" + input + "</p>";
    return output;
  }

}
