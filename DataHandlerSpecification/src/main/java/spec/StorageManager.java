package spec;


public class StorageManager {
	private static DataHandlerSpecification storage;
	
	public static void registerExporter(DataHandlerSpecification storage) {
		StorageManager.storage = storage;
	}
	
	public static DataHandlerSpecification getExporter(String directoryName) {
		storage.setDirectoryName(directoryName);
		return storage;
	}
}
