package org.example.client.deprecated_movies;

import java.io.InputStream;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JsonToClientSaleDtoConversor {

	public static ClientSaleDto toClientSaleDto(InputStream jsonSale) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonSale);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode movieObject = (ObjectNode) rootNode;

				JsonNode saleIdNode = movieObject.get("saleId");
				Long saleId = (saleIdNode != null) ? saleIdNode.longValue() : null;

				Long movieId = movieObject.get("movieId").longValue();
				String movieUrl = movieObject.get("movieUrl").textValue().trim();
				String expirationDate = movieObject.get("expirationDate").textValue().trim();
	
				return new ClientSaleDto(saleId, movieId, LocalDateTime.parse(expirationDate), movieUrl);

			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

}