package com.recipe.recipetest.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
    }

    @Test
    void getId() {
        String testIdValue = "4L";
        category.setId(testIdValue);
        assertEquals(testIdValue, category.getId());
    }

    @Test
    void getDescription() {

    }

    @Test
    void getRecipes() {
    }
}