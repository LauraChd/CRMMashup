package fr.univangers.client.service.exceptions;

/**
 * Exception levée en cas d'erreur de parsing
 */
@SuppressWarnings("serial")
public class ParsingException extends RuntimeException {

    /**
     * Constructeur avec message et cause
     *
     * @param message Message d'erreur
     * @param cause   Cause de l'erreur
     */
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur avec cause
     *
     * @param cause Cause de l'erreur
     */
    public ParsingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructeur avec message
     *
     * @param message Message d'erreur
     */
    public ParsingException(String message) {
        super(message);
    }

    /**
     * Constructeur par défaut
     */
    public ParsingException() {
    }

}