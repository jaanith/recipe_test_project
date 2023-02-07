package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.converters.IngredientCommandToIngredient;
import com.recipe.recipetest.converters.IngredientToIngredientCommand;
import com.recipe.recipetest.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.repositories.RecipeRepository;
import com.recipe.recipetest.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImp implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImp(IngredientToIngredientCommand ingredientToIngredientCommand,
                                IngredientCommandToIngredient ingredientCommandToIngredient,
                                RecipeRepository recipeRepository,
                                UnitOfMeasureRepository unitOfMeasureRepository,
                                UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()){
            return ingredientNotFound("recipe id not found. Id: ", recipeId);

        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();

        if(ingredientOptional.isEmpty()){
            return ingredientNotFound("Ingredient id not found. Id: ", ingredientId);
        }
        Ingredient ingredientFound = ingredientOptional.get();
        ingredientFound.setRecipeId(recipeId);
        return ingredientToIngredientCommand.convert(ingredientFound);
    }

    public IngredientCommand ingredientNotFound(String message, String id) {
        log.error(message + id);
        return null;
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());
        UnitOfMeasure ingredientUnitOfMeasure = getUnitOfMeasureFromIngredientCommand(command);
        if(ingredientUnitOfMeasure == null) {
            throw new RuntimeException("UOM NOT FOUND");
        }
        command.setUnitOfMeasure(unitOfMeasureToUnitOfMeasureCommand.convert(ingredientUnitOfMeasure));
        if(recipeOptional.isEmpty()){
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                //Already got from database
                ingredientFound.setUnitOfMeasure(ingredientUnitOfMeasure);
            } else {
                //add new Ingredient

                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                assert ingredient != null;
                //ingredient.setRecipe(recipe);
                ingredient.setRecipeId(recipe.getId());
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if(savedIngredientOptional.isEmpty()){
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            //to do check for fail
            if (savedIngredientOptional.isPresent()) {
                Ingredient savedIngredient = savedIngredientOptional.get();
                if(savedIngredient.getRecipeId() == null || savedIngredient.getRecipeId().equals("")) {
                    savedIngredient.setRecipeId(savedRecipe.getId());
                }
                return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            }
            return null;
        }

    }

    @Override
    public void deleteById(String recipeId, String idToDelete) {

        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
    }

    private UnitOfMeasure getUnitOfMeasureFromIngredientCommand(IngredientCommand ingredientCommand) {
        String idValue = ingredientCommand.getUnitOfMeasureId();
        String actualValue = ingredientCommand.getUnitOfMeasureValue();
        Optional<UnitOfMeasure> uom = unitOfMeasureRepository.findById(idValue);

        if (uom.isPresent()) {
            return uom.get();
        }
        Optional<UnitOfMeasure> uom2 = unitOfMeasureRepository.findAll().stream().filter(x -> x.getId().equals(idValue)).findFirst();
        if (uom2.isPresent()) {
            return uom2.get();
        }
        if (actualValue != null) {
            Optional<UnitOfMeasure> uom3 = unitOfMeasureRepository.findByDescription(actualValue);
            if (uom3.isPresent()) {
                return  uom3.get();
            }
        }
        return null;
    }
}
