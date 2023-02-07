package com.recipe.recipetest.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Getter
@Setter
@Document(collection = "unitofmeasures")
public class UnitOfMeasure {

    @MongoId
    private String Id;
    private String description;
}
