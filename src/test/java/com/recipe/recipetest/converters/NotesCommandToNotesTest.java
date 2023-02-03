package com.recipe.recipetest.converters;

import com.recipe.recipetest.commands.NotesCommand;
import com.recipe.recipetest.domain.Notes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class NotesCommandToNotesTest {

    public static final Long ID_VALUE = 1L;
    public static final String RECIPE_NOTES = "Notes";
    NotesCommandToNotes converter;

    @BeforeEach
    void setUp() {
        converter = new NotesCommandToNotes();
    }

    @Test
    public void testNullParameter()  {
        Assertions.assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        Assertions.assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    void convert() {
        //given
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(ID_VALUE);
        notesCommand.setRecipeNotes(RECIPE_NOTES);

        //when
        Notes notes = converter.convert(notesCommand);

        //then
        Assertions.assertNotNull(notes);
        Assertions.assertEquals(ID_VALUE, notes.getId());
        Assertions.assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
    }
}