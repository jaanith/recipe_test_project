package com.recipe.recipetest.repositories;

import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {

    @Query("{ 'id' : ?0 }")
    Optional<Recipe> findById(String id);

    Optional<Recipe> findByDescription(String description);
}
