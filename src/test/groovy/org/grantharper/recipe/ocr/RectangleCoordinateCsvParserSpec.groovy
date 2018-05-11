package org.grantharper.recipe.ocr

import com.opencsv.CSVReader
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import spock.lang.Specification

import java.awt.Rectangle
import java.nio.file.Paths

class RectangleCoordinateCsvParserSpec extends Specification
{

  CsvToBean<RectangleCoordinates> csvToBean;
  String csvFilePath = "src/test/resources/rectangles.csv"
  RectangleCoordinateCsvParser rectangleCoordinateCsvParser;

  def setup()
  {
    rectangleCoordinateCsvParser = new RectangleCoordinateCsvParser()
    rectangleCoordinateCsvParser.setCsvFilename(csvFilePath)
  }

  def "get list of rectangles"()
  {
    when: "csv parser is asked for rectangles"
    List<Rectangle> rectangles = rectangleCoordinateCsvParser.getRectangles()

    then: "rectangles are parsed out"
    rectangles.get(0).x == 10
    rectangles.get(0).width == 960
    rectangles.get(0).y == 0
    rectangles.get(0).height == 300
    rectangles.get(1).y == 350

  }

  def "get list of rectangle coordinates"()
  {
    when: "csv parser is called"
    List<RectangleCoordinates> rectangleCoordinates =
            rectangleCoordinateCsvParser.getCoordinateList()

    then: "the coordinate list"
    rectangleCoordinates.get(0).x1 == 10
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
