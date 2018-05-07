# Recipe OCR

This project was built to perform optical character recognition for the purpose of importing recipes to search using the web app [recipe-index](https://github.com/grantharper/recipe-index) app

## Elasticsearch Index Setup

Start up elasticsearch on the local machine

Create the index mapping using `recipe-index-mapping.json`

`curl -X PUT localhost:9200/recipe -H 'Content-Type: application/json' --data-binary @recipe-index-mapping.json`

To delete the index if you want to start over

`curl -X DELETE localhost:9200/recipe`

## OCR Setup and Build

Copy in the Tesseract data into the tessdata directory. This data can be found [here](https://github.com/tesseract-ocr/tessdata/tree/3.04.00)

Configure the application to process the image files of your choice
* Update `src/main/resources/application.properties`. 
  * Rectangle settings define the pixel boundaries for the OCR
  * The recipe parser implementation class is the class that will be used to parse the text from the OCR into the Recipe json
* Write your own custom class to parse the text. By implementing the `RecipeParser` or `RecipeParserExtended` interface, or extending the skeletal implementation class `RecipeParserAbstract`, you can plug your own class into the framework.
* The example implementation `RecipeParserSurLaTable` can be used as a guide of how to write a custom class

`gradle build`

To execute the data pipeline and translate jpeg recipe images into json documents, you can use the following methods:

* Run the `RecipeApplication` main class in your IDE
* `gradle run`
* Copy the jar file ending in `-all` to a directory of your choice and run `java -jar <jar-file-name>`

Follow the menu instructions to process the image files and load them to a locally-running elasticsearch index

The mid-pipeline text output will be sent to the output directories defined in `application.properties`. Make sure to review the output after each step since the OCR has a tendency to pick up extra notes and splotches as characters which will then show up in the recipe json.

Once you have edited the txt files, run the application again. For any image file that already has a corresponding text file, the image will not go through OCR again. The parser will simply read in the existing text file and translate that to json.

## Elasticsearch Searches

After loading the index, the following command should show that documents are loaded to the index

`curl localhost:9200/recipe/_cat/indices?v`

To search recipes based on their ingredients

`curl localhost:9200/recipe/_search?q=ingredients:asparagus`



