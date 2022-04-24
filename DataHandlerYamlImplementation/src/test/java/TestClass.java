import java.util.HashMap;
import java.util.Map;

import model.Entity;
import service.DataHandlerYamlImplementation;
import spec.StorageManager;

public class TestClass {

	DataHandlerYamlImplementation dataHandlerYamlImplementation = new DataHandlerYamlImplementation();
	String path = "E:\\Alya\\RAF\\Asistent\\Arhiva\\SK\\Data Handler\\data handler examples";

	public void yamlTest() {
		System.out.println(dataHandlerYamlImplementation.getFileService().importData(path));

		StorageManager.registerExporter(dataHandlerYamlImplementation);
		Map<String, String> map = new HashMap<>();
		map.put("zani", "manje");
	}
	
}
