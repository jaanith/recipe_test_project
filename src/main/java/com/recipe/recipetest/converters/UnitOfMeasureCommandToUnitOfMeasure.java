package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.UnitOfMeasureCommand;
import com.recipe.recipetest.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {


    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand source) {
        if (source == null) {
            return null;
        }

        final UnitOfMeasure uom = new UnitOfMeasure();
        if(source.getId() != null) {
            uom.setId(source.getId());
        }
        uom.setDescription(source.getDescription());
        return uom;
    }
}
