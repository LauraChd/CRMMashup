package fr.univangers.deprecated_movies.model.exceptions;

@SuppressWarnings("serial")
public class InputValidationException extends Exception {

    public InputValidationException(String message) {
        super(message);
    }
}