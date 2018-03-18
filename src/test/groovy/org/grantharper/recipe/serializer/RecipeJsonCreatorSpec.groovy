package org.grantharper.recipe.serializer

import org.grantharper.recipe.domain.Recipe
import org.grantharper.recipe.serializer.RecipeJsonCreator

import spock.lang.Specification

class RecipeJsonCreatorSpec extends Specification
{
  
  RecipeJsonCreator creator = new RecipeJsonCreator()
  
  List<String> ingredients = ["3 cups crumbled Greek feta","2/3 cup extra—virgin olive oil, plus more for serving",
                              "Zest and juice of 1 lemon","Freshly ground black pepper"]
  
  Recipe recipe = new Recipe("Whipped Feta with Grilled Pita Bread", 
    "3 cups", 
    ingredients,
    "To prepare the spread: Place feta, olive oil, lemon zest, juice, and a few grinds of black pepper\n"
    + "into the bowl of a food processor and blend until smooth and creamy.\n"
    + "To serve: Transfer the mixture into a serving bowl, drizzle with olive oil, and sprinkle with za'atar.")
  
  def "Create Json From Recipe"() {
    given: "recipe json creator"
    
    when: "recipe is marshalled into Json"
    String json = creator.generateOutput(recipe)
        
    then: "json is created"
    json.contains("{")
    json.contains(recipe.title)
    
  }


}
