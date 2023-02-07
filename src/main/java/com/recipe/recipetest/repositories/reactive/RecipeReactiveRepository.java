package com.recipe.recipetest.repositories.reactive;

import com.recipe.recipetest.domain.Recipe;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {

    @Query("{ 'id' : ?0 }")
    Mono<Recipe> findById(String id);

    Mono<Recipe> findByDescription(String description);

}
