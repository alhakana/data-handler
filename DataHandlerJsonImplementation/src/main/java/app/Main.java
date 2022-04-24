package app;

import spec.DataHandlerSpecification;
import spec.StorageManager;

public class Main {

	public static void main(String[] args) {
		try {
			Class.forName("app.JsonRegister");

			String path = "E:\\Alya\\RAF\\Asistent\\Arhiva\\SK\\Data Handler\\DataHandler examples\\Json";
			DataHandlerSpecification dataHandlerImplementation = StorageManager.getExporter(path);
			System.out.println(dataHandlerImplementation.getFileService().read());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
