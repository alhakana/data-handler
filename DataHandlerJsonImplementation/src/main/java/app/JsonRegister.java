package app;

import service.DataHandlerJsonImplementation;
import spec.StorageManager;

public class JsonRegister {

    static {
        StorageManager.registerExporter(new DataHandlerJsonImplementation());
    }

}
