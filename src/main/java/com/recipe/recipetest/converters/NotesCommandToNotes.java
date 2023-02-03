package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.NotesCommand;
import com.recipe.recipetest.domain.Notes;
import lombok.Synchronized;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes>{

    @Synchronized
    @Nullable
    @Override
    public Notes convert(NotesCommand source) {
        if(source == null) {
            return null;
        }

        final Notes notes = new Notes();
        if(source.getId() != null) {
            notes.setId(source.getId());
        }
        notes.setRecipeNotes(source.getRecipeNotes());
        return notes;
    }
}
