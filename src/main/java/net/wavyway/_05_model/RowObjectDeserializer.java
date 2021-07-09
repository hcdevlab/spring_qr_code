package net.wavyway._05_model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class RowObjectDeserializer extends JsonDeserializer<RowObject> {

    @Override
    public RowObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        String name = node.get("userName").textValue();
        String date = node.get("date").textValue();
        String qrCode = node.get("qrCode").textValue();
        String latCoord = node.get("latCoordinate").textValue();
        String longCoord = node.get("longCoordinate").textValue();

        return new RowObject(name, date, qrCode, latCoord, longCoord);
    }
}