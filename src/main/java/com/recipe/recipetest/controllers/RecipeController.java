package com.recipe.recipetest.controllers;

import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/{id}/show")
    public String ShowById(@PathVariable String id, Model model) {
        Long idLong = Long.parseLong(id);
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
        RecipeCommand newRecipeCommand = recipeService.findCommandById(Long.parseLong(id));
        if (newRecipeCommand != null) {
            model.addAttribute("recipe", newRecipeCommand );
            return "recipe/recipeform";
        }
        return "redirect:index";
    }

    @PostMapping({"/recipe", "/recipe/"})
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        //log.debug("Got recipe command");
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(recipeCommand);
        //log.debug("Saved as" + savedCommand.getId());
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id){
        log.debug("Deleting id: " + id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
}
