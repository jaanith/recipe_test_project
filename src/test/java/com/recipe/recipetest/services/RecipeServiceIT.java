package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.converters.RecipeCommandToRecipe;
import com.recipe.recipetest.converters.RecipeToRecipeCommand;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


//@DataMongoTest
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeReactiveRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    //@Transactional
    @Test
    public void testSaveOfDescription()   {
        //given
        Iterable<Recipe> recipes = recipeRepository.findAll().collectList().block();
        assert recipes != null;
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        //when
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand).block();

        //then
        Assertions.assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        Assertions.assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        Assertions.assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        Assertions.assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }
}
