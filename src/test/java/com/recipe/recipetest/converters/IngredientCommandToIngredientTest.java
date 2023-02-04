package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import com.recipe.recipetest.domain.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;


class IngredientCommandToIngredientTest {

    public static final BigDecimal AMOUNT = new BigDecimal("1");
    public static final String DESCRIPTION = "Cheeseburger";
    public static final Long ID_VALUE = 1L;
    public static final Long UOM_ID = 2L;

    IngredientCommandToIngredient converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void testNullObject(){
        Assertions.assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        Assertions.assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(ID_VALUE);
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        command.setUnitOfMeasure(unitOfMeasureCommand);

        //when
        Ingredient ingredient = converter.convert(command);

        //then
        Assertions.assertNotNull(ingredient);
        Assertions.assertNotNull(ingredient.getUnitOfMeasure());
        Assertions.assertEquals(ID_VALUE, ingredient.getId());
        Assertions.assertEquals(AMOUNT, ingredient.getAmount());
        Assertions.assertEquals(DESCRIPTION, ingredient.getDescription());
        Long newId = ingredient.getUnitOfMeasure().getId();
        Assertions.assertEquals(UOM_ID, newId);
    }

    @org.junit.Test
    public void convertWithNullUOM() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(ID_VALUE);
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();


        //when
        Ingredient ingredient = converter.convert(command);

        //then
        Assertions.assertNotNull(ingredient);
        Assertions.assertNull(ingredient.getUnitOfMeasure());
        Assertions.assertEquals(ID_VALUE, ingredient.getId());
        Assertions.assertEquals(AMOUNT, ingredient.getAmount());
        Assertions.assertEquals(DESCRIPTION, ingredient.getDescription());
    }
}