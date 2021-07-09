package net.wavyway._05_model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class WrapperRowObjectDeserializer extends JsonDeserializer<WrapperRowObject>
{
	@Override
	public WrapperRowObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
	{
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		return null;
	}
}
