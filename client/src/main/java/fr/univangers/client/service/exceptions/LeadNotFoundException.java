package fr.univangers.client.service.exceptions;

/**
 * Exception levée lorsque le lead n'est pas trouvé (erreur 404)
 */
public class LeadNotFoundException extends Exception {

    private String leadId;

    public LeadNotFoundException(String leadId) {
        super("Lead with id=\"" + leadId + "\" does not exists");
        this.leadId = leadId;
    }

    public String getMovieId() {
        return leadId;
    }

    public void setMovieId(Long leadId) {
        this.leadId = LeadNotFoundException.this.leadId;
    }
}
