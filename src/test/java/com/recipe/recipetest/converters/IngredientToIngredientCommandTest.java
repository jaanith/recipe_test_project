package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.domain.UnitOfMeasure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


class IngredientToIngredientCommandTest {

    public static final Recipe RECIPE = new Recipe();
    public static final BigDecimal AMOUNT = new BigDecimal("1");
    public static final String DESCRIPTION = "Cheeseburger";
    public static final String UOM_ID = "2L";
    public static final String ID_VALUE = "1L";

    IngredientToIngredientCommand converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void testNullConvert()  {
        Ingredient newIngredient = null;
        Assertions.assertNull(converter.convert(newIngredient));
    }

    @Test
    public void testEmptyObject() {
        Assertions.assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    public void testConvertNullUOM(){
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_VALUE);
        ingredient.setRecipe(RECIPE);
        ingredient.setAmount(AMOUNT);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setUnitOfMeasure(null);
        //when
        IngredientCommand ingredientCommand = converter.convert(ingredient);
        //then
        //assert ingredientCommand != null;
        Assertions.assertNull(ingredientCommand.getUnitOfMeasure());
        Assertions.assertEquals(ID_VALUE, ingredientCommand.getId());
        Assertions.assertEquals(AMOUNT, ingredientCommand.getAmount());
        Assertions.assertEquals(DESCRIPTION, ingredientCommand.getDescription());
    }

    @Test
    public void testConvertWithUom()  {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_VALUE);
        ingredient.setRecipe(RECIPE);
        ingredient.setAmount(AMOUNT);
        ingredient.setDescription(DESCRIPTION);

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);

        ingredient.setUnitOfMeasure(uom);
        //when
        IngredientCommand ingredientCommand = converter.convert(ingredient);
        //then
        Assertions.assertEquals(ID_VALUE, ingredientCommand.getId());
        Assertions.assertNotNull(ingredientCommand.getUnitOfMeasure());
        Assertions.assertEquals(UOM_ID, ingredientCommand.getUnitOfMeasure().getId());
        // assertEquals(RECIPE, ingredientCommand.get);
        Assertions.assertEquals(AMOUNT, ingredientCommand.getAmount());
        Assertions.assertEquals(DESCRIPTION, ingredientCommand.getDescription());
    }
}