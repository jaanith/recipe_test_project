package com.recipe.recipetest.controllers;

import com.recipe.recipetest.services.IngredientService;
import org.springframework.stereotype.Controller;

@Controller
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }
}
