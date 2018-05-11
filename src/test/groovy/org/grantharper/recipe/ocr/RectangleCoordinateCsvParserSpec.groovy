package org.grantharper.recipe.ocr

import com.opencsv.CSVReader
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import spock.lang.Specification

class RectangleCoordinateCsvParserSpec extends Specification
{

  CSVReader csvReader;
  CsvToBean<RectangleCoordinates> csvToBean;
  String csvFilePath = "src/test/resources/rectangles.csv"

  def setup()
  {
  }

  def "parse rectangle csv into rectangle coordinates"()
  {

    when: "csv parsing is performed"
    List<RectangleCoordinates> coordinates = new CsvToBeanBuilder(new FileReader(csvFilePath))
            .withType(RectangleCoordinates.class).build().parse()

    then: "coordinates are found"
    coordinates.get(0).x1 == 10
    coordinates.get(0).x2 == 970
    coordinates.get(1).y2 == 616


  }
}
