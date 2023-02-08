package com.recipe.recipetest.services;

import com.recipe.recipetest.domain.Recipe;
import com.recipe.recipetest.exceptions.NotFoundException;
import com.recipe.recipetest.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImp implements ImageService {

    private final RecipeReactiveRepository recipeRepository;

    public ImageServiceImp(RecipeReactiveRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

        Mono<Recipe> recipeMono = recipeRepository.findById(recipeId)
                .map(recipe -> {
                    Byte[] byteObjects = new Byte[0];
                    try {
                        byteObjects = new Byte[file.getBytes().length];

                        int i = 0;
                        for (byte b : file.getBytes()) {
                            byteObjects[i++] = b;
                        }
                        recipe.setImage(byteObjects);
                        return recipe;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
        recipeRepository.save(recipeMono.block()).block();
        return Mono.empty();
    }

    @Override
    public Mono<Recipe> saveRecipeImage(String recipeId, FilePart image) {

        log.debug("Saving image for recipe: " + recipeId);
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id: " + recipeId + " not found")))
                .zipWith(DataBufferUtils.join(image.content()))
                .map(recipeAndImage -> {
                    recipeAndImage.getT1().setImage(ArrayUtils.toObject(recipeAndImage.getT2().asByteBuffer().array()));
                    return recipeAndImage.getT1();
                })
                .flatMap(recipeRepository::save);

    }
}
