package com.recipe.recipetest.converters;

import com.recipe.recipetest.exceptions.BadNumberFormatException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StringToIntegerConverter implements Converter<String, Integer> {

    private static final Pattern p = Pattern.compile("\\d+");

    @Override
    public Integer convert(String source) {

        int i = 0;
        try {
            Matcher m = p.matcher(source);
            if (m.find()) {
                i = Integer.parseInt(m.group());
            }
        } catch (Exception ex) {
            throw new BadNumberFormatException("\"" + source + "\": unrecognizable number format");
        }
        //System.out.println("Source: "+ source + " ->i: " + i);
        return i;
    }
}
