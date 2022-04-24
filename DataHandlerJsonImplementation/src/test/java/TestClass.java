import org.junit.Test;
import service.DataHandlerJsonImplementation;
import spec.StorageManager;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


public class TestClass {

    DataHandlerJsonImplementation dataHandlerYamlImplementation = new DataHandlerJsonImplementation();
    String path = "E:\\Alya\\RAF\\Asistent\\Arhiva\\SK\\Data Handler\\data handler examples\\Json";

    @Test
    public void test() {
        System.out.println(dataHandlerYamlImplementation.getFileService().importData(path));

        StorageManager.registerExporter(dataHandlerYamlImplementation);
        Map<String, String> map = new HashMap<>();
        map.put("zani", "manje");
//        assertEquals
    }
}
