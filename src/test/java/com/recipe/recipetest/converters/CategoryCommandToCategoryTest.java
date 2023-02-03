package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.CategoryCommand;
import com.recipe.recipetest.domain.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryCommandToCategoryTest {

    public static final Long ID_VALUE = 1L;
    public static final String DESCRIPTION = "description";
    CategoryCommandToCategory converter;


    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void testNullObject() {
        Assertions.assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject()  {
        Assertions.assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        //given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        categoryCommand.setDescription(DESCRIPTION);

        //when
        Category category = converter.convert(categoryCommand);

        //then
        assert category != null;
        Assertions.assertEquals(ID_VALUE, category.getId());
        Assertions.assertEquals(DESCRIPTION, category.getDescription());
    }
}