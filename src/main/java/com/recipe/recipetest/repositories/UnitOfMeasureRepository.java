package com.recipe.recipetest.repositories;

import com.recipe.recipetest.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface UnitOfMeasureRepository extends MongoRepository<UnitOfMeasure,String> {

    Optional<UnitOfMeasure> findByDescription(String description);
}
