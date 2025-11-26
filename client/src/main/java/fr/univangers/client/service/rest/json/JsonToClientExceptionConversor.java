package fr.univangers.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import fr.univangers.client.service.exceptions.ForbiddenActionException;
import fr.univangers.client.service.utils.exceptions.InputValidationException;
import fr.univangers.client.service.utils.exceptions.InstanceNotFoundException;
import fr.univangers.client.service.utils.exceptions.ObjectMapperFactory;
import fr.univangers.client.service.utils.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {

    /**
     * Requête incorrecte
     * @param ex
     * @return
     * @throws ParsingException
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

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    /**
     * Requête qui n'a pas fonctionné
     * @param ex
     * @return
     * @throws ParsingException
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

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }




    /**
     * Convertit une erreur HTTP 500 en exception applicative,
     * avec traitement spécial pour /addLead.
     */
    public static Exception fromServerErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);

            if (!rootNode.isObject()) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            }

            String path = rootNode.has("path") ? rootNode.get("path").asText() : "";
            String error = rootNode.has("error") ? rootNode.get("error").asText() : "Internal Server Error";

            // Cas spécifique pour /addLead
            if ("/addLead".equals(path) && "Internal Server Error".equalsIgnoreCase(error)) {
                return new ForbiddenActionException("Ajout impossible, lead déjà existant");
            }

            // Sinon renvoyer une exception générique
            String timestamp = rootNode.has("timestamp") ? rootNode.get("timestamp").asText() : "";
            int status = rootNode.has("status") ? rootNode.get("status").asInt() : 500;

            return new ForbiddenActionException(
                    "Erreur serveur (" + status + ") sur le chemin : " + path
                            + " | error = " + error
                            + " | timestamp = " + timestamp
            );

        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }



    private static ForbiddenActionException toForbiddenActionException(String message) {
        return new ForbiddenActionException(message);
    }
}
