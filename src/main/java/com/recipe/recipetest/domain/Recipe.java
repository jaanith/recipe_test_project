package com.recipe.recipetest.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;

    @Lob
    private String directions;

    @Lob
    private Byte[] image;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    //Recipe owns note:if recipe is deleted, then note is also deleted
    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    //The meaning of CascadeType.ALL is that the persistence will propagate (cascade) all EntityManager operations
    // (PERSIST, REMOVE, REFRESH, MERGE, DETACH) to the relating entities.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "recipe_category",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Ingredient addIngredient(Ingredient newIngredient) {
        ingredients.add(newIngredient);
        newIngredient.setRecipe(this);
        return newIngredient;
    }

    public Notes addNote(Notes newNote) {
        newNote.setRecipe(this);
        this.notes = newNote;
        return newNote;
    }
}
