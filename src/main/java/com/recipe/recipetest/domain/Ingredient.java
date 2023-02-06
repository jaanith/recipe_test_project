package com.recipe.recipetest.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
//@Document
@NoArgsConstructor
public class Ingredient {

    //@Id
    private String id = UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;
    private Recipe recipe;
    @DBRef
    private UnitOfMeasure unitOfMeasure;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure unit) {
        this.description = description;
        this.amount = amount;
        this.unitOfMeasure = unit;
    }
}
