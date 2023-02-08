package com.recipe.recipetest.services;

import com.recipe.recipetest.domain.Recipe;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<Void> saveImageFile(String recipeId, MultipartFile file);
    Mono<Recipe> saveRecipeImage(String recipeId, FilePart image);
}
