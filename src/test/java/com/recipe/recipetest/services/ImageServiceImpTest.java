package com.recipe.recipetest.services;

import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.repositories.RecipeRepository;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ImageServiceImpTest {
    @Mock
    RecipeReactiveRepository recipeRepository;

    ImageService imageService;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImp(recipeRepository);
    }

    @Test
    void saveImageFile() throws IOException {

        //given
        String id = "1L";
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                "Spring Framework Guru".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        imageService.saveImageFile(id, multipartFile);

        //then
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        Assertions.assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);

    }
}