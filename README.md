# Recipe OCR

This project was built to perform optical character recognition for the purpose of importing recipes into the [recipe-index](https://github.com/grantharper/recipe-index) app

# Build

Copy in the Tesseract data into the tessdata directory. This data can be found [here](https://github.com/tesseract-ocr/tessdata/tree/3.04.00)

Copy a jpeg recipe image into the `data/input` directory with the pageId as the file name (e.g. 45.jpg for pageId=45)

To build `gradlew build`

To execute `gradlew run`

The output will be sent to the `data/output/json` directory (e.g. 45.json)


