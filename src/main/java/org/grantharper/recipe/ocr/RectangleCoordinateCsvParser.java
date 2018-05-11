package org.grantharper.recipe.ocr;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RectangleCoordinateCsvParser
{

  private static final Logger logger = LogManager.getLogger(RectangleCoordinateCsvParser.class);

  private String csvFilename;

  @Value("${csv.filename}")
  public void setCsvFilename(String csvFilename)
  {
    this.csvFilename = csvFilename;
  }

  public List<Rectangle> getRectangles()
  {
    List<Rectangle> rectangles = new ArrayList<>();
    List<RectangleCoordinates> coordinates = getCoordinateList();
    for (RectangleCoordinates coordinate : coordinates) {
      rectangles.add(coordinate.determineRectangleDimensions());
    }
    return rectangles;
  }

  List<RectangleCoordinates> getCoordinateList()
  {
    try {
      List<RectangleCoordinates> coordinates = new CsvToBeanBuilder(new FileReader(csvFilename))
              .withType(RectangleCoordinates.class).build().parse();
      return coordinates;
    } catch (FileNotFoundException e) {
      logger.error("Csv Parsing Failed", e);
      throw new RuntimeException("Csv Parsing Failed");
    }
  }

}
