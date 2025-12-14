package fr.univangers.client.service.exceptions;

/**
 * Exception levée lorsque l'action demandée n'est pas possible/autorisée
 */
public class ForbiddenActionException extends Exception {
    public ForbiddenActionException(String reason) {
        super("Action impossible : " + reason);
    }
}
