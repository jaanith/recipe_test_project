package com.recipe.recipetest.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    private String id = UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;
    private String recipeId;
    private UnitOfMeasure unitOfMeasure;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure unit) {
        this.description = description;
        this.amount = amount;
        this.unitOfMeasure = unit;
    }
}
