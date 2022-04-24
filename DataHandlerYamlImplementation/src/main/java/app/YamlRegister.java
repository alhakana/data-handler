package app;

import service.DataHandlerYamlImplementation;
import spec.StorageManager;

public class YamlRegister {

    static {
        StorageManager.registerExporter(new DataHandlerYamlImplementation());
    }

}
