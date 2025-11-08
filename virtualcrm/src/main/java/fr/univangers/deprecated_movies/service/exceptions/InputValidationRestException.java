package fr.univangers.deprecated_movies.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InputValidationRestException extends Exception {

    public InputValidationRestException(String message) {
        super(message);
    }
}