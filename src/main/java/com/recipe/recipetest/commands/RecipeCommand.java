package com.recipe.recipetest.commands;

import com.recipe.recipetest.domain.Difficulty;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.Set;




@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {

    private Long id;

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
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<CategoryCommand> categories = new HashSet<>();
    private Byte[] image;


}
