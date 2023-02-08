package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.converters.RecipeCommandToRecipe;
import com.recipe.recipetest.converters.RecipeToRecipeCommand;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RecipeServiceImp implements RecipeService {
    private final RecipeReactiveRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImp(RecipeReactiveRepository recipeRepository,
                            RecipeCommandToRecipe recipeCommandToRecipe,
                            RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    public Flux<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String l) {
        return recipeRepository.findById(l).map(x -> {
            x.setIngredients(AddIdToIngredients(x.getIngredients(), x.getId()));
            return x;
        });
    }

    List<Ingredient> AddIdToIngredients(List<Ingredient> ingredients, String id) {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (int i = 0; i< ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            ingredient.setRecipeId(id);
            ingredientList.add(ingredient);
        }
        return ingredientList;
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String anyLong) {
        return recipeRepository
                .findById(anyLong)
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        return recipeRepository.findById(command.getId()).flatMap(
                recipe -> {
                    Recipe newRecipe = recipeCommandToRecipe.convert(command);
                    assert newRecipe != null;
                    newRecipe.setCategories(recipe.getCategories());
                    newRecipe.setIngredients(recipe.getIngredients());
                    return recipeRepository.save(newRecipe);
                }
        ).map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String idToDelete) {
        return recipeRepository.deleteById(idToDelete).then();
    }
}
