package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.converters.RecipeCommandToRecipe;
import com.recipe.recipetest.converters.RecipeToRecipeCommand;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.exceptions.NotFoundException;
import com.recipe.recipetest.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImp implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImp(RecipeRepository recipeRepository,
                            RecipeCommandToRecipe recipeCommandToRecipe,
                            RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    public Set<Recipe> getRecipes() {
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    @Transactional
    public Recipe findById(String l) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(l);
        if(recipeOptional.isEmpty()){
            throw new NotFoundException("Recipe not found. for ID value of " + l.toString());
        }
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(String anyLong) {
        return recipeToRecipeCommand.convert(findById(anyLong));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
        if (detachedRecipe != null) {
            Recipe savedRecipe = recipeRepository.insert(detachedRecipe);
            if (savedRecipe.getId() != null) {
                return recipeToRecipeCommand.convert(savedRecipe);
            }
        }
        return null;
    }

    /*
    As MongoDB is not capable of generating id's, then this has to do it
     */
    String getUniqueId() {
        Set<String> recipes = recipeRepository.findAll().stream().map(x->x.getId()).collect(Collectors.toSet());
        int i = 0;
        while(i < 1000) {
            String newId = UUID.randomUUID().toString();
            if (!recipes.contains(newId)) {
                return newId;
            }
            i++;
        }
        return null;
    }
    @Override
    public void deleteById(String idToDelete) {
        recipeRepository.deleteById(idToDelete);
    }
}
