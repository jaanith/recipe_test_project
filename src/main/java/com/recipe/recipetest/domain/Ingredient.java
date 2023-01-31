package com.recipe.recipetest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;

    @ManyToOne
    private Recipe recipe;

    //Load in every time (just to show that this is possible)
    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure unitOfMeasure;
}
