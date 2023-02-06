package com.recipe.recipetest.domain;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document
public class UnitOfMeasure {

    @Id
    private String Id;
    private String description;
}
