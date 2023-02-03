package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.CategoryCommand;
import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.commands.NotesCommand;
import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.domain.Difficulty;
import com.recipe.recipetest.domain.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeCommandToRecipeTest {
    public static final Long RECIPE_ID = 1L;
    public static final String COOK_TIME_S = "5";
    public static final String PREP_TIME_S = "7";
    public static final String SERVINGS_S = "3";
    public static final Integer COOK_TIME = Integer.valueOf(COOK_TIME_S);
    public static final Integer PREP_TIME = Integer.valueOf(PREP_TIME_S);
    public static final String DESCRIPTION = "My Recipe";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = Integer.valueOf(SERVINGS_S);
    public static final String SOURCE = "Source";
    public static final String URL = "Some URL";
    public static final Long CAT_ID_1 = 1L;
    public static final Long CAT_ID2 = 2L;
    public static final Long INGRED_ID_1 = 3L;
    public static final Long INGRED_ID_2 = 4L;
    public static final Long NOTES_ID = 9L;
    RecipeCommandToRecipe converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeCommandToRecipe(new CategoryCommandToCategory(),
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
                new NotesCommandToNotes(), new StringToIntegerConverter());
    }

    @Test
    public void testNullObject() {
        Assertions.assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        Assertions.assertNotNull(converter.convert(new RecipeCommand()));
    }

    @Test
    void convert() {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setCookTime(COOK_TIME_S);
        recipeCommand.setPrepTime(PREP_TIME_S);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setDifficulty(DIFFICULTY);
        recipeCommand.setDirections(DIRECTIONS);
        recipeCommand.setServings(SERVINGS_S);
        recipeCommand.setSource(SOURCE);
        recipeCommand.setUrl(URL);

        NotesCommand notes = new NotesCommand();
        notes.setId(NOTES_ID);

        recipeCommand.setNotes(notes);

        CategoryCommand category = new CategoryCommand();
        category.setId(CAT_ID_1);

        CategoryCommand category2 = new CategoryCommand();
        category2.setId(CAT_ID2);

        recipeCommand.getCategories().add(category);
        recipeCommand.getCategories().add(category2);

        IngredientCommand ingredient = new IngredientCommand();
        ingredient.setId(INGRED_ID_1);

        IngredientCommand ingredient2 = new IngredientCommand();
        ingredient2.setId(INGRED_ID_2);

        recipeCommand.getIngredients().add(ingredient);
        recipeCommand.getIngredients().add(ingredient2);

        //when
        Recipe recipe  = converter.convert(recipeCommand);

        Assertions.assertNotNull(recipe);
        Assertions.assertEquals(RECIPE_ID, recipe.getId());
        Assertions.assertEquals(COOK_TIME, recipe.getCookTime());
        Assertions.assertEquals(PREP_TIME, recipe.getPrepTime());
        Assertions.assertEquals(DESCRIPTION, recipe.getDescription());
        Assertions.assertEquals(DIFFICULTY, recipe.getDifficulty());
        Assertions.assertEquals(DIRECTIONS, recipe.getDirections());
        Assertions.assertEquals(SERVINGS, recipe.getServings());
        Assertions.assertEquals(SOURCE, recipe.getSource());
        Assertions.assertEquals(URL, recipe.getUrl());
        Assertions.assertEquals(NOTES_ID, recipe.getNotes().getId());
        Assertions.assertEquals(2, recipe.getCategories().size());
        Assertions.assertEquals(2, recipe.getIngredients().size());
    }
}