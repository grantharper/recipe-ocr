package org.grantharper.recipe.ocr;


import com.opencsv.bean.CsvBindByName;

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

}
