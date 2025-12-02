package fr.univangers.client.service.exceptions;

public class ForbiddenActionException extends Exception {
    public ForbiddenActionException(String reason) {
        super("Action impossible : " + reason);
    }
}
