package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.NotesCommand;
import com.recipe.recipetest.domain.Notes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotesToNotesCommandTest {

    public static final String ID_VALUE = "1L";
    public static final String RECIPE_NOTES = "Notes";
    NotesToNotesCommand converter;

    @BeforeEach
    void setUp() {
        converter = new NotesToNotesCommand();
    }

    @Test
    public void testNull() {
        Assertions.assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        Assertions.assertNotNull(converter.convert(new Notes()));
    }

    @Test
    void convert() {
        //given
        Notes notes = new Notes();
        notes.setId(ID_VALUE);
        notes.setRecipeNotes(RECIPE_NOTES);

        //when
        NotesCommand notesCommand = converter.convert(notes);

        //then
        assert notesCommand != null;
        Assertions.assertEquals(ID_VALUE, notesCommand.getId());
        Assertions.assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
    }
}