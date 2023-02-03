package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {


    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureConverter;

    public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter) {
        this.unitOfMeasureConverter = uomConverter;
    }

    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if (source == null) {
            return null;
        }

        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());

        if(source.getRecipeId() != null){
            Recipe recipe = new Recipe();
            recipe.setId(source.getRecipeId());
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }

        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUnitOfMeasure(unitOfMeasureConverter.convert(source.getUnitOfMeasureCommand()));
        return ingredient;
    }
}
