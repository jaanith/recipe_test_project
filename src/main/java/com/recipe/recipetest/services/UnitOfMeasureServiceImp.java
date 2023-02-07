package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import com.recipe.recipetest.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.repositories.UnitOfMeasureRepository;
import com.recipe.recipetest.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImp implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImp(UnitOfMeasureReactiveRepository unitOfMeasureRepository,
                                   UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {
        return unitOfMeasureRepository.findAll().map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }

}
