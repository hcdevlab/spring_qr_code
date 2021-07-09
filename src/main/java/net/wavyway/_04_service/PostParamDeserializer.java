package net.wavyway._04_service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.TreeMap;


public class PostParamDeserializer extends JsonDeserializer<TreeMap<String, String>>
{
	@Override
	public TreeMap<String, String> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException
	{
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);

		TreeMap<String, String> treeMap = new TreeMap<>();
		treeMap.put("firstParam", node.get("firstParam").textValue());
		treeMap.put("lastParam", node.get("lastParam").textValue());

		return treeMap;
	}
}
