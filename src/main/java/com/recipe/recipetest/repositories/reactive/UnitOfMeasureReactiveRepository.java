package com.recipe.recipetest.repositories.reactive;

import com.recipe.recipetest.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {

    @Query("{ 'id' : ?0 }")
    Mono<UnitOfMeasure> findById(String id);

    Mono<UnitOfMeasure> findByDescription(String description);
}
