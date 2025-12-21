package fr.univangers.client.service.exceptions;

/**
 * Exception levée en cas d'erreur de validation des entrées
 */
@SuppressWarnings("serial")
public class InputValidationException extends Exception {

    /**
     * Constructeur avec message
     *
     * @param message Message d'erreur
     */
    public InputValidationException(String message) {
        super(message);
    }
}