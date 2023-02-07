package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> listAllUoms();

}
