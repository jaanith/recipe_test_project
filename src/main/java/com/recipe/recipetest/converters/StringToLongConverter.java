package com.recipe.recipetest.converters;

import com.recipe.recipetest.exceptions.BadNumberFormatException;
import com.recipe.recipetest.exceptions.NotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class StringToLongConverter implements Converter<String, Long> {


    @Override
    public Long convert(String source) {
        Long i;
        try {
            i = Long.parseLong(source);
            //Matcher m = p.matcher(source);
            //if (m.find()) {
            //    i = Integer.parseInt(m.group());
            //}
        } catch (Exception ex) {
            throw new BadNumberFormatException("\"" + source + "\": unrecognizable number format");
        }
        return i;
    }
}
