package com.recipe.recipetest.commands;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private String id;
    private String recipeId;
    @NotEmpty
    private String description;
    @NotNull
    @Min(1)
    private BigDecimal amount;
    private String unitOfMeasureId;
    private String unitOfMeasureValue;
    private UnitOfMeasureCommand unitOfMeasure;
}
