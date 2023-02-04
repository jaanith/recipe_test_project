package com.recipe.recipetest.controllers;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.converters.StringToLongConverter;
import com.recipe.recipetest.services.RecipeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RecipeController {

    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";

    private final RecipeService recipeService;
    private final StringToLongConverter stringToLongConverter;

    public RecipeController(RecipeService recipeService, StringToLongConverter stringToLongConverter) {
        this.recipeService = recipeService;
        this.stringToLongConverter = stringToLongConverter;
    }

    @RequestMapping("/recipe/{id}/show")
    public String ShowById(@PathVariable String id, Model model) {
        Long idLong = stringToLongConverter.convert(id);
        model.addAttribute("recipe", recipeService.findById(idLong));
        return "recipe/show";
    }

    @RequestMapping({"/recipe", "/recipe/"})
    public String recipeDefaultPage(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }

    @RequestMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        log.debug("Directed to recipe form");
        return "recipe/recipeform";
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        Long idLong = stringToLongConverter.convert(id);
        RecipeCommand newRecipeCommand = recipeService.findCommandById(idLong);
        if (newRecipeCommand != null) {
            model.addAttribute("recipe", newRecipeCommand );
            return "recipe/recipeform";
        }
        return "redirect:index";
    }

    @PostMapping({"/recipe", "/recipe/"})
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return RECIPE_RECIPEFORM_URL;
        }

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id){
        log.debug("Deleting id: " + id);
        Long idLong = stringToLongConverter.convert(id);
        recipeService.deleteById(idLong);
        return "redirect:/";
    }
}
