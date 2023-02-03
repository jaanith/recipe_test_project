package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.converters.IngredientCommandToIngredient;
import com.recipe.recipetest.converters.IngredientToIngredientCommand;
import com.recipe.recipetest.repositories.RecipeRepository;
import com.recipe.recipetest.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImp implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImp(IngredientToIngredientCommand ingredientToIngredientCommand,
                                IngredientCommandToIngredient ingredientCommandToIngredient,
                                RecipeRepository recipeRepository,
                                UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        return null;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        return null;
    }
}
