package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.converters.RecipeCommandToRecipe;
import com.recipe.recipetest.converters.RecipeToRecipeCommand;
import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Example of integration tests
class RecipeServiceImpTest {

    RecipeServiceImp recipeService;

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    AutoCloseable closeable;

    @BeforeEach
    public void setUp() throws Exception {
        //https://www.arhohuttunen.com/junit-5-mockito/
        //Variant 1
        //recipeRepository = Mockito.mock(RecipeRepository.class);
        //Variant 2
        closeable = MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImp(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        List<Recipe> initialRecipes = new ArrayList<>();
        initialRecipes.add(recipe);
        when(recipeRepository.findAll()).thenReturn(Flux.fromIterable(initialRecipes));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();
        //assertEquals(0, recipes.size());
        assert recipes != null;
        assertEquals(1, recipes.size());
        //Check if recipeRepository findAll method was called only once
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void getRecipeByIdTest()  {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1L").block();

        Assertions.assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getRecipeCommandByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1L");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1L").block();

        Assertions.assertNotNull(commandById, "Null recipe returned");
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }
}