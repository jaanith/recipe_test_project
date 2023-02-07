package com.recipe.recipetest.repositories;

import com.recipe.recipetest.bootstrap.RecipeBootstrap;
import com.recipe.recipetest.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@DataMongoTest
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
class UnitOfMeasureRepositoryTest {

    /*
    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RecipeRepository recipeRepository;
    */

    @BeforeEach
    void setUp() {
        //recipeRepository.deleteAll();
        //unitOfMeasureRepository.deleteAll();
        //categoryRepository.deleteAll();
        //RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);
        //recipeBootstrap.onApplicationEvent(null);
    }

    @Test
    void findByDescription() {
        //Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        //assertEquals("Teaspoon", unitOfMeasureOptional.get().getDescription());
    }

    @Test
    void findByDescriptionCup() {
        //Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Cup");
        //assertEquals("Cup", unitOfMeasureOptional.get().getDescription());
    }
}