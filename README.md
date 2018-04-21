# Recipe OCR

This project was built to perform optical character recognition for the purpose of importing recipes into the [recipe-index](https://github.com/grantharper/recipe-index) app

## Build

Copy in the Tesseract data into the tessdata directory. This data can be found [here](https://github.com/tesseract-ocr/tessdata/tree/3.04.00)

Copy a jpeg recipe image into the `data/input` directory with the pageId as the file name (e.g. 45.jpg for pageId=45)

To build `gradlew build`

To execute the data pipeline and translate jpeg recipe images into json documents, run the Application class

Assumptions:
* This application is currently only build to use the `RecipeParserSurLaTable`
* Files must be named according to the pageId that is desired in the elasticsearch index

The output will be sent to the `data/output/json` directory (e.g. 45.json)

## Elasticsearch Index Setup

Start up elasticsearch on the local machine

Create the index mapping using `recipe-index-mapping.json`

`curl -X PUT localhost:9200/recipe -H 'Content-Type: application/json' --data-binary @recipe-index-mapping.json`

Run the LoadElasticSearchApp class to load all of the files in the `data/output/json` directory to the index

## Elasticsearch Searches

Below are a few searches that should return some results and illustrate the intention of the index to be able to locate recipes based on their ingredients

`curl localhost:9200/recipe/_search?q=ingredients:asparagus`



