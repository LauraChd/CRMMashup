package fr.univangers.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import fr.univangers.client.service.utils.exceptions.InputValidationException;
import fr.univangers.client.service.utils.exceptions.InstanceNotFoundException;
import fr.univangers.client.service.utils.exceptions.ObjectMapperFactory;
import fr.univangers.client.service.utils.exceptions.ParsingException;

import java.io.InputStream;

/**
 * Classe qui permet de convertir des erreurs JSON provenant
 * du serveur en exceptions Java côté client.
 */
public class JsonToClientExceptionConversor {

    /**
     * Convertit une erreur HTTP 400 (Bad Request) en exception Java.
     *
     * @param ex flux JSON contenant les informations d'erreur
     * @return une exception correspondant à l'erreur
     * @throws ParsingException si le JSON est invalide ou inconnu
     */
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Convertit un JSON vers une InputValidationException.
     *
     * @param rootNode racine du JSON
     * @return exception de validation d’entrée
     */
    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    /**
     * Convertit une erreur HTTP 404 (Not Found) en exception Java.
     *
     * @param ex flux JSON contenant les informations d'erreur
     * @return une exception InstanceNotFoundException
     * @throws ParsingException si le JSON est invalide
     */
    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Convertit un JSON vers une InstanceNotFoundException.
     *
     * @param rootNode racine du JSON
     * @return exception InstanceNotFoundException
     */
    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

}
