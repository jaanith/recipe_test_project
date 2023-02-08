package com.recipe.recipetest.controllers;

import com.recipe.recipetest.exceptions.NotFoundException;
import com.recipe.recipetest.services.ImageService;
import com.recipe.recipetest.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ImageController {

    private  final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService,
                           RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("recipe/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public Mono<String> uploadImage(@PathVariable String recipeId, @RequestPart("imagefile") Mono<FilePart> image) {

        log.debug("Uploading an image for recipe: " + recipeId);

        return image.map(imageToSave -> imageToSave)
                .flatMap(imageToSave -> imageService.saveRecipeImage(recipeId, imageToSave))
                .thenReturn("redirect:/recipe/" + recipeId + "/show");
    }


    @GetMapping(value = "/recipe/{recipeId}/recipeimage", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<Void> renderRecipeImage(@PathVariable String recipeId, ServerHttpResponse response) {

        return recipeService.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found in database!")))
                .map(recipe -> response.bufferFactory().wrap(ArrayUtils.toPrimitive(recipe.getImage())))
                .flatMap(image -> response.writeWith(Flux.just(image)));

    }
}
