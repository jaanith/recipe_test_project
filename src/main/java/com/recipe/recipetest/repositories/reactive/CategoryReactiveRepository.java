package com.recipe.recipetest.repositories.reactive;

import com.recipe.recipetest.domain.Category;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {

    @Query("{ 'id' : ?0 }")
    Mono<Category> findById(String id);

    Mono<Category> findByDescription(String description);
}
