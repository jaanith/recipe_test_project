package com.recipe.recipetest.services;

import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import com.recipe.recipetest.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.recipe.recipetest.domain.UnitOfMeasure;
import com.recipe.recipetest.repositories.UnitOfMeasureRepository;
import com.recipe.recipetest.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImpTest {

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService service;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    AutoCloseable closeable;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        service = new UnitOfMeasureServiceImp(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {
        //given
        List<UnitOfMeasure> unitOfMeasures = new ArrayList<>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId("1L");
        unitOfMeasures.add(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId("2L");
        unitOfMeasures.add(uom2);

        //when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);
        when(unitOfMeasureRepository.findAll()).thenReturn(Flux.just(uom1, uom2));

        //when
        List<UnitOfMeasureCommand> commands = service.listAllUoms().collectList().block();

        //then
        Assertions.assertEquals(2, commands.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}