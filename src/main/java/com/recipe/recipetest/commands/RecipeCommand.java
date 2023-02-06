package com.recipe.recipetest.commands;

import com.recipe.recipetest.domain.Difficulty;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {

    private String id;

    @NotEmpty
    @Size(min = 3, max = 255)
    private String description;
    private String prepTime;
    private String cookTime;
    private String servings;
    private String source;
    @URL
    private String url;
    @NotEmpty
    private String directions;
    private List<IngredientCommand> ingredients = new ArrayList<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private List<CategoryCommand> categories = new ArrayList<>();
    private Byte[] image;


}
