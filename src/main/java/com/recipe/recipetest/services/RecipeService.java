package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface RecipeService {
    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(String l);

    Mono<RecipeCommand> findCommandById(String anyLong);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);

    Mono<Void> deleteById(String idToDelete);
}
