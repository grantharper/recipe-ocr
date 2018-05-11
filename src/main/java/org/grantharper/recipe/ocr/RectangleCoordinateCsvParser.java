package org.grantharper.recipe.ocr;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grantharper.recipe.converter.PngToTextConverter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

public class RectangleCoordinateCsvParser
{

  private static final Logger logger = LogManager.getLogger(RectangleCoordinateCsvParser.class);

  public List<RectangleCoordinates> getCoordinateList(Path csvFilePath)
  {
    try {
      List<RectangleCoordinates> coordinates = new CsvToBeanBuilder(new FileReader(csvFilePath.toString()))
              .withType(RectangleCoordinates.class).build().parse();
      return coordinates;
    } catch (FileNotFoundException e) {
      logger.error("Csv Parsing Failed", e);
      throw new RuntimeException("Csv Parsing Failed");
    }
  }

}
