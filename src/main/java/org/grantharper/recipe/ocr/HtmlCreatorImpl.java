package org.grantharper.recipe.ocr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public List<String> generateHtmlPage()
  {
    return surroundWithHtml5Document(createMultiLineParagraphs(this.recipeText));
  }

  protected List<String> surroundWithHtml5Document(List<String> input)
  {
    try(Stream<String> topLines = Files.lines(Paths.get("html/top.html"));
    Stream<String> bottomLines = Files.lines(Paths.get("html/bottom.html"));)
    {
      List<String> header = topLines.collect(Collectors.toList());
      List<String> footer = bottomLines.collect(Collectors.toList());
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
    return "<p>" + input + "</p>";
  }

  protected String surroundStandardLineBreaksWithDivs(String input)
  {
    return "<div class='ocr-recipe'>" + input + "</div>";
  }

  protected String surroundTitleWithDivs(String input)
  {
    return "<div class='ocr-title'>" + input + "</div>";
  }

  @Override
  public List<String> generateDivs()
  {
    String[] output = this.recipeText.split("\n");
    List<String> divFinalOutput = new ArrayList<>();
    List<String> divOutput = Arrays.asList(output)
        .stream()
        .map(String::trim)
        .filter(s -> !s.equals(""))
        .collect(Collectors.toList());
    for (int i = 0; i < divOutput.size(); i++)
    {
      if (i == 0)
        divFinalOutput.add(surroundTitleWithDivs(divOutput.get(i)));
      else
        divFinalOutput.add(surroundStandardLineBreaksWithDivs(divOutput.get(i)));

    }
    return divFinalOutput;
  }

}
