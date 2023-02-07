package com.recipe.recipetest.repositories;

import com.recipe.recipetest.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.Optional;

//@Repository
public interface UnitOfMeasureRepository extends MongoRepository<UnitOfMeasure,String> {

    @Query("{ 'id' : ?0 }")
    Optional<UnitOfMeasure> findById(String id);

    Optional<UnitOfMeasure> findByDescription(String description);
}
