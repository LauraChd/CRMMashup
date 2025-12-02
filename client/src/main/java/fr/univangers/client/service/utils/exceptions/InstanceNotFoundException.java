package fr.univangers.client.service.utils.exceptions;

/**
 * Exception levée lorsqu'une instance n'est pas trouvée.
 */
@SuppressWarnings("serial")
public class InstanceNotFoundException extends Exception {

    private Object instanceId;
    private String instanceType;

    /**
     * Constructeur.
     *
     * @param instanceId   ID de l'instance.
     * @param instanceType Type de l'instance.
     */
    public InstanceNotFoundException(Object instanceId, String instanceType) {

        super("Instance not found (identifier = '" + instanceId + "' - type = '"
                + instanceType + "')");
        this.instanceId = instanceId;
        this.instanceType = instanceType;

    }

    /**
     * Retourne l'ID de l'instance.
     *
     * @return L'ID de l'instance.
     */
    public Object getInstanceId() {
        return instanceId;
    }

    /**
     * Retourne le type de l'instance.
     *
     * @return Le type de l'instance.
     */
    public String getInstanceType() {
        return instanceType;
    }
}