package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.converters.IngredientCommandToIngredient;
import com.recipe.recipetest.converters.IngredientToIngredientCommand;
import com.recipe.recipetest.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import com.recipe.recipetest.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

        Recipe recipe = recipeRepository.findById(command.getRecipeId()).share().block();
        UnitOfMeasure ingredientUnitOfMeasure = getUnitOfMeasureFromIngredientCommand(command).share().block();
        if(ingredientUnitOfMeasure == null) {
            throw new RuntimeException("UOM NOT FOUND");
        }
        command.setUnitOfMeasure(unitOfMeasureToUnitOfMeasureCommand.convert(ingredientUnitOfMeasure));
        if(recipe == null){
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        } else {
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
                ingredient.setRecipeId(recipe.getId());
                recipe.addIngredient(ingredient);
            }
            Recipe savedRecipe = recipeRepository.save(recipe).share().block();

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
        return recipeRepository.findById(recipeId)
                .flatMap(recipe -> {
                    recipe.setIngredients(
                            recipe.getIngredients()
                                    .stream()
                                    .filter(ingredient -> !ingredient.getId().equals(idToDelete))
                                    .collect(Collectors.toList())
                    );
                    return recipeRepository.save(recipe);
                }).then();
    }

    private Mono<UnitOfMeasure> getUnitOfMeasureFromIngredientCommand(IngredientCommand ingredientCommand) {
        String idValue = ingredientCommand.getUnitOfMeasureId();
        String actualValue = ingredientCommand.getUnitOfMeasureValue();
        return unitOfMeasureRepository.findById(idValue)
                .switchIfEmpty(unitOfMeasureRepository.findAll().filter(uom -> uom.getId().equals(idValue)).single())
                .switchIfEmpty(unitOfMeasureRepository.findByDescription(actualValue));
    }
}
