package org.grantharper.recipe.ocr;


import com.opencsv.bean.CsvBindByName;

import java.awt.*;

public class RectangleCoordinates
{
  @CsvBindByName
  private int x1;

  @CsvBindByName
  private int x2;

  @CsvBindByName
  private int y1;

  @CsvBindByName
  private int y2;

  public RectangleCoordinates(){}

  public int getX1()
  {
    return x1;
  }

  public int getX2()
  {
    return x2;
  }

  public int getY1()
  {
    return y1;
  }

  public int getY2()
  {
    return y2;
  }

  public Rectangle determineRectangleDimensions()
  {
    int rectangleWidth = x2 - x1;
    int rectangleHeight = y2 - y1;
    return new Rectangle(x1, y1, rectangleWidth, rectangleHeight);
  }

}
