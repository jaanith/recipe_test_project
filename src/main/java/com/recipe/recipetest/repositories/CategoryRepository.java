package com.recipe.recipetest.repositories;

import com.recipe.recipetest.domain.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByDescription(String description);
}
