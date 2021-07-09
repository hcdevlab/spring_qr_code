package unit_test;

import net.wavyway._04_service.DataService;
import net.wavyway._05_model.RowObject;

import java.util.Date;

public class RowObjectBuilder {

    private final RowObject instance = new RowObject();

    public static RowObjectBuilder create() {
        String username = "user test";
        String date = new Date().toString();
        String qrCode = "QrCode";
        String latCoord = "lat coord";
        String longCoord = "long coord";

        RowObjectBuilder builder = new RowObjectBuilder();

        builder.instance.setUserName(username);
        builder.instance.setDate(date);
        builder.instance.setQrCode(qrCode);
        builder.instance.setLatCoord(latCoord);
        builder.instance.setLongCoord(longCoord);

        return builder;
    }

    public RowObject persist(DataService dataService) {
        dataService.insert(instance);
        return instance;
    }
}
