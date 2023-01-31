package com.recipe.recipetest.repositories;

import com.recipe.recipetest.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
