package com.recipe.recipetest.controllers;


import com.recipe.recipetest.commands.RecipeCommand;
import com.recipe.recipetest.converters.StringToLongConverter;
import com.recipe.recipetest.services.ImageService;
import com.recipe.recipetest.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private  final ImageService imageService;
    private final RecipeService recipeService;
    private final StringToLongConverter stringToLongConverter;

    public ImageController(ImageService imageService,
                           RecipeService recipeService,
                           StringToLongConverter stringToLongConverter) {
        this.imageService = imageService;
        this.recipeService = recipeService;
        this.stringToLongConverter = stringToLongConverter;
    }

    @GetMapping("recipe/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model){
        Long idLong =stringToLongConverter.convert(id);
        model.addAttribute("recipe", recipeService.findCommandById(idLong));

        return "recipe/imageuploadform";
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){
        Long idLong =stringToLongConverter.convert(id);
        imageService.saveImageFile(idLong, file);

        return "redirect:/recipe/" + id + "/show";
    }

    @GetMapping("recipe/{id}/recipeimage")
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        Long idLong =stringToLongConverter.convert(id);
        RecipeCommand recipeCommand = recipeService.findCommandById(idLong);

        if (recipeCommand.getImage() != null) {
            byte[] byteArray = new byte[recipeCommand.getImage().length];
            int i = 0;

            for (Byte wrappedByte : recipeCommand.getImage()){
                byteArray[i++] = wrappedByte; //auto unboxing
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
