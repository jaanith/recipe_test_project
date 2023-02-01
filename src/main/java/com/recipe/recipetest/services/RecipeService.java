package com.recipe.recipetest.services;

import com.recipe.recipetest.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
