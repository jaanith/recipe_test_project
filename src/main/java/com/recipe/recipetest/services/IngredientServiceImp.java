package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.IngredientCommand;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImp implements IngredientService {


    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        return null;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        return null;
    }
}
