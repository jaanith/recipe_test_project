package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.converters.RecipeCommandToRecipe;
import com.recipe.recipetest.converters.RecipeToRecipeCommand;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.exceptions.NotFoundException;
import com.recipe.recipetest.repositories.RecipeRepository;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
        Recipe foundRecipe = recipeRepository.findById(l).block();
        if(foundRecipe == null){
            throw new NotFoundException("Recipe not found. for ID value of " + l);
        }
        foundRecipe.setIngredients(AddIdToIngredients(foundRecipe.getIngredients(), foundRecipe.getId()));
        return Mono.just(foundRecipe);
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
        return recipeRepository
                .save(Objects.requireNonNull(recipeCommandToRecipe.convert(command)))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String idToDelete) {
        recipeRepository.deleteById(idToDelete).block();
        return Mono.empty();
    }
}
