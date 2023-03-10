package com.recipe.recipetest.controllers;

import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;



import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndexControllerTest {
    IndexController controller;

    @Mock
    Model model;

    @Mock
    RecipeService recipeService;

    AutoCloseable closeable;
    MockMvc mockMvc;


    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    void getIndexPage() {

        //Given
        HashSet<Recipe> initialRecipes = new HashSet<>();
        initialRecipes.add(new Recipe());
        initialRecipes.add(new Recipe());

        when(recipeService.getRecipes()).thenReturn(initialRecipes);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.getIndexPage(model);

        //then
        //Check if model addAttribute method is called exactly one time
        //use matcher like "eq" and anySet()
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> setReceived = argumentCaptor.getValue();
        assertEquals(2, setReceived.size());
        //Check if recipeService method get recipes is called exactly one time
        verify(recipeService, times(1)).getRecipes();
        //Check if indexController returns a string
        assertEquals("index", viewName);
    }
}