package app;

import spec.DataHandlerSpecification;
import spec.Storage;
import spec.StorageManager;

public class Main {

	public static void main(String[] args) {
		try {
			Class.forName("app.YamlRegister");

			String path = "E:\\Alya\\RAF\\Asistent\\Arhiva\\SK\\Data Handler\\DataHandler examples\\Yaml";
			DataHandlerSpecification dataHandlerYamlImplementation = StorageManager.getExporter(path);
			System.out.println(dataHandlerYamlImplementation.getFileService().read());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
