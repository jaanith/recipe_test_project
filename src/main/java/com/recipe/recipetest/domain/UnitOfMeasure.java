package com.recipe.recipetest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UnitOfMeasure {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long Id;

    private String description;
}
