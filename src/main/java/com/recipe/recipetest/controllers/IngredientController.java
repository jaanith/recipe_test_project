package com.recipe.recipetest.controllers;

import com.recipe.recipetest.commands.IngredientCommand;
import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import com.recipe.recipetest.converters.StringToLongConverter;
import com.recipe.recipetest.services.IngredientService;
import com.recipe.recipetest.services.RecipeService;
import com.recipe.recipetest.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;
    private final StringToLongConverter stringToLongConverter;

    public IngredientController(IngredientService ingredientService,
                                RecipeService recipeService,
                                UnitOfMeasureService unitOfMeasureService,
                                StringToLongConverter stringToLongConverter) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.stringToLongConverter = stringToLongConverter;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list for recipe id: " + recipeId);
        Long idLong = stringToLongConverter.convert(recipeId);
        // use command object to avoid lazy load errors in Thymeleaf.
        model.addAttribute("recipe", recipeService.findCommandById(idLong));

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model){
        Long idLong = stringToLongConverter.convert(recipeId);
        //make sure we have a good id value
        RecipeCommand recipeCommand = recipeService.findCommandById(idLong);


        //need to return back parent id for hidden form property
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        model.addAttribute("ingredient", ingredientCommand);

        //init uom
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("uomList",  unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String viewRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id, Model model){
        Long idRecipe = stringToLongConverter.convert(recipeId);
        Long idIngredient = stringToLongConverter.convert(id);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(idRecipe, idIngredient));

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id, Model model){
        Long idRecipe = stringToLongConverter.convert(recipeId);
        Long idIngredient = stringToLongConverter.convert(id);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(idRecipe, idIngredient));

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }



    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved receipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredientById(@PathVariable String recipeId, @PathVariable String id){
        Long idRecipe = stringToLongConverter.convert(recipeId);
        Long idIngredient = stringToLongConverter.convert(id);
        log.debug("Deleting ingredient id: " + id + " from recipe " + recipeId);
        ingredientService.deleteById(idRecipe, idIngredient);
        return "redirect:/recipe/{recipeId}/ingredients";
    }
}
