package com.recipe.recipetest.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Getter
@Setter
@Document(collection = "categories")
public class Category {

    @Id
    private String id;
    private String description;
    @DBRef
    private Set<Recipe> recipes;
}
