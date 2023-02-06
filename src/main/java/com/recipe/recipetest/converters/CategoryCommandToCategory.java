package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.CategoryCommand;
import com.recipe.recipetest.domain.Category;
import lombok.Synchronized;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category>{

    @Synchronized
    @Nullable
    @Override
    public Category convert(CategoryCommand source) {
        if (source == null) {
            return null;
        }

        final Category category = new Category();
        if (category.getId() != null && !category.getId().equals(""))
        category.setId(source.getId());
        category.setDescription(source.getDescription());
        return category;
    }
}
