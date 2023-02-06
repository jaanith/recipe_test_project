package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RecipeToRecipeCommandTest {

    public static final String RECIPE_ID = "1L";
    public static final String COOK_TIME_S = "5";
    public static final String PREP_TIME_S = "7";
    public static final String SERVINGS_S = "3";
    public static final Integer COOK_TIME = Integer.valueOf(COOK_TIME_S);
    public static final Integer PREP_TIME = Integer.valueOf(PREP_TIME_S);
    public static final Integer SERVINGS = Integer.valueOf(SERVINGS_S);
    public static final String DESCRIPTION = "My Recipe";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final String SOURCE = "Source";
    public static final String URL = "Some URL";
    public static final String CAT_ID_1 = "1L";
    public static final String CAT_ID2 = "2L";
    public static final String INGRED_ID_1 = "3L";
    public static final String INGRED_ID_2 = "4L";
    public static final String NOTES_ID = "9L";
    RecipeToRecipeCommand converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeToRecipeCommand(
                new CategoryToCategoryCommand(),
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
                new NotesToNotesCommand());
    }

    @Test
    public void testNullObject()  {
        Assertions.assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject()  {
        Assertions.assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    void convert() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setDescription(DESCRIPTION);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setDirections(DIRECTIONS);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);

        Notes notes = new Notes();
        notes.setId(NOTES_ID);

        recipe.setNotes(notes);

        Category category = new Category();
        category.setId(CAT_ID_1);

        Category category2 = new Category();
        category2.setId(CAT_ID2);

        recipe.getCategories().add(category);
        recipe.getCategories().add(category2);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGRED_ID_1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(INGRED_ID_2);

        recipe.getIngredients().add(ingredient);
        recipe.getIngredients().add(ingredient2);

        //when
        RecipeCommand command = converter.convert(recipe);

        //then
        Assertions.assertNotNull(command);
        Assertions.assertEquals(RECIPE_ID, command.getId());
        Assertions.assertEquals(COOK_TIME_S, command.getCookTime());
        Assertions.assertEquals(PREP_TIME_S, command.getPrepTime());
        Assertions.assertEquals(DESCRIPTION, command.getDescription());
        Assertions.assertEquals(DIFFICULTY, command.getDifficulty());
        Assertions.assertEquals(DIRECTIONS, command.getDirections());
        Assertions.assertEquals(SERVINGS_S, command.getServings());
        Assertions.assertEquals(SOURCE, command.getSource());
        Assertions.assertEquals(URL, command.getUrl());
        Assertions.assertEquals(NOTES_ID, command.getNotes().getId());
        Assertions.assertEquals(2, command.getCategories().size());
        Assertions.assertEquals(2, command.getIngredients().size());

    }
}