package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import com.recipe.recipetest.converters.*;
import com.recipe.recipetest.domain.Ingredient;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import com.recipe.recipetest.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class IngredientServiceImpTest {

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    IngredientService ingredientService;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        IngredientCommandToIngredient ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
        IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientService = new IngredientServiceImp(ingredientToIngredientCommand, ingredientCommandToIngredient,
                recipeRepository, unitOfMeasureRepository, new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    void findByRecipeIdAndIngredientId() {
    }

    @Test
    void saveIngredientCommand() {
    }

    @Test
    public void findByRecipeIdAndRecipeIdHappyPath()  {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1L");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("1L");

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3L");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Mono<Recipe> recipeOptional = Mono.just(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1L", "3L").block();

        //when
        assert ingredientCommand != null;
        Assertions.assertEquals("3L", ingredientCommand.getId());
        Assertions.assertEquals("1L", ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyString());
    }


    @Test
    public void testSaveRecipeCommand() {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId("3L");
        command.setRecipeId("2L");
        command.setUnitOfMeasureId("1");
        command.setUnitOfMeasureValue("uom");

        Mono<Recipe> recipeOptional = Mono.just(new Recipe());
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId("1");
        uom.setDescription("uom");
        UnitOfMeasureToUnitOfMeasureCommand uomConverter = new UnitOfMeasureToUnitOfMeasureCommand();
        UnitOfMeasureCommand uomCommand = uomConverter.convert(uom);
        command.setUnitOfMeasure(uomCommand);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3L");

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        when(unitOfMeasureRepository.findById(anyString())).thenReturn(Mono.just(uom));
        when(unitOfMeasureRepository.findAll()).thenReturn(Flux.fromIterable(List.of(uom)));
        when(unitOfMeasureRepository.findByDescription(anyString())).thenReturn(Mono.just(uom));

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        //then
        assert savedCommand != null;
        Assertions.assertEquals("3L", savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }

    @Test
    public void testDeleteById()  {
        //given
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3L");
        recipe.addIngredient(ingredient);
        ingredient.setRecipeId(recipe.getId());

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(any())).thenReturn(Mono.just(recipe));

        //when
        ingredientService.deleteById("1L", "3L").subscribe();

        //then
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}