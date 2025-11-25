package fr.univangers.client.service.exceptions;

public class InvalidParametersException extends Exception {

    public InvalidParametersException() {
        super("Requête invalide");
    }
}
