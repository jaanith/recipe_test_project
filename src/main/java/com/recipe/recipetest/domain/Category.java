package com.recipe.recipetest.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Getter
@Setter
@Document(collection = "categories")
public class Category {

    @MongoId
    private String id;
    private String description;
    @DBRef
    private Set<Recipe> recipes;
}
