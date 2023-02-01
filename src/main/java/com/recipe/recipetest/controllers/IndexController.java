package com.recipe.recipetest.controllers;

import com.recipe.recipetest.domain.Category;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.repositories.CategoryRepository;
import com.recipe.recipetest.repositories.UnitOfMeasureRepository;
import com.recipe.recipetest.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
