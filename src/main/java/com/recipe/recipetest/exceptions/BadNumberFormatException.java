package com.recipe.recipetest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadNumberFormatException extends NumberFormatException {

    public BadNumberFormatException() {
        super();
    }

    public BadNumberFormatException(String message) {
        super(message);
    }

    // public BadNumberFormatException(String message, Throwable cause) {
    //    super(message, cause);
    //}
}
