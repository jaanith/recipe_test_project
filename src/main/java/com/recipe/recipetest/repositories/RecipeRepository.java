package com.recipe.recipetest.repositories;

import com.recipe.recipetest.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
