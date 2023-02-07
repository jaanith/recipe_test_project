package com.recipe.recipetest.repositories;

import com.recipe.recipetest.domain.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.Optional;

//@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    @Query("{ 'id' : ?0 }")
    Optional<Category> findById(String id);

    Optional<Category> findByDescription(String description);
}
