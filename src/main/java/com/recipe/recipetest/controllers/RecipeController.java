package com.recipe.recipetest.controllers;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.services.RecipeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RecipeController {

    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
    private static final String RECIPE_SHOW = "recipe/show";
    private static final String INITIAL_PAGE = "redirect:/index";
    private final RecipeService recipeService;
    private WebDataBinder webDataBinder;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{id}/show")
    public String ShowById(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return RECIPE_SHOW;
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return RECIPE_RECIPEFORM_URL;
    }


    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id) );
        return RECIPE_RECIPEFORM_URL;
    }

    @PostMapping({"/recipe", "/recipe/"})
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, Model model) {
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return RECIPE_RECIPEFORM_URL;
        }
        model.addAttribute("recipe", recipeService.saveRecipeCommand(recipeCommand));
        return RECIPE_SHOW;
    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id){
        recipeService.deleteById(id).subscribe();
        return INITIAL_PAGE;
    }
}
