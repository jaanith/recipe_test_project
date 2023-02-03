package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import com.recipe.recipetest.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.recipe.recipetest.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UnitOfMeasureServiceImp implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImp(UnitOfMeasureRepository unitOfMeasureRepository,
                                   UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return null;
    }
}
