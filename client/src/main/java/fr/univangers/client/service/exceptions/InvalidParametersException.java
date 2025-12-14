package fr.univangers.client.service.exceptions;

/**
 * Exception levée lorsque les paramètres sont invalides
 */
public class InvalidParametersException extends Exception {

    public InvalidParametersException() {
        super("Requête invalide");
    }
}
