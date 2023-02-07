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
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import com.recipe.recipetest.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IngredientServiceImp implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
    private final RecipeReactiveRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    public IngredientServiceImp(IngredientToIngredientCommand ingredientToIngredientCommand,
                                IngredientCommandToIngredient ingredientCommandToIngredient,
                                RecipeReactiveRepository recipeRepository,
                                UnitOfMeasureReactiveRepository unitOfMeasureRepository,
                                UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient-> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    assert command != null;
                    command.setRecipeId(recipeId);
                    return  command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId()).blockOptional();
        UnitOfMeasure ingredientUnitOfMeasure = getUnitOfMeasureFromIngredientCommand(command);
        if(ingredientUnitOfMeasure == null) {
            throw new RuntimeException("UOM NOT FOUND");
        }
        command.setUnitOfMeasure(unitOfMeasureToUnitOfMeasureCommand.convert(ingredientUnitOfMeasure));
        if(recipeOptional.isEmpty()){
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
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

            Recipe savedRecipe = recipeRepository.save(recipe).block();

            assert savedRecipe != null;
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
                return Mono.just(Objects.requireNonNull(ingredientToIngredientCommand.convert(savedIngredientOptional.get())));
            }
            return null;
        }
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String idToDelete) {
        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);
        Recipe recipe = recipeRepository.findById(recipeId).block();
        if(recipe != null){
            log.debug("found recipe");
            List<Ingredient> ingredients = recipe.getIngredients()
                    .stream()
                    .filter(x -> !x.getId().equals(idToDelete))
                    .collect(Collectors.toList());
            recipe.setIngredients(ingredients);
            recipeRepository.save(recipe).block();
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
        return Mono.empty();
    }

    private UnitOfMeasure getUnitOfMeasureFromIngredientCommand(IngredientCommand ingredientCommand) {
        String idValue = ingredientCommand.getUnitOfMeasureId();
        String actualValue = ingredientCommand.getUnitOfMeasureValue();
        Optional<UnitOfMeasure> uom = unitOfMeasureRepository.findById(idValue).blockOptional();
        if (uom.isPresent()) {
            return uom.get();
        }
        UnitOfMeasure uom2 = unitOfMeasureRepository.findAll().filter(x -> x.getId().equals(idValue)).blockFirst();
        if (uom2 != null) {
            return uom2;
        }
        if (actualValue != null) {
            UnitOfMeasure uom3 = unitOfMeasureRepository.findAll().filter(x -> x.getDescription().equals(actualValue)).blockFirst();
            if (uom3 != null) {
                return uom3;
            }
        }
        return null;
    }
}
