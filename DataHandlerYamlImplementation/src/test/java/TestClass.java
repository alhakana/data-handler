import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.YamlRegister;
import model.Entity;
import org.junit.Test;
import spec.DataHandlerSpecification;
import spec.StorageManager;
import static org.junit.jupiter.api.Assertions.*;

public class TestClass {

	String path = "src\\test\\resources";
	YamlRegister yamlRegister = new YamlRegister();
	DataHandlerSpecification dataHandlerSpecification = StorageManager.getExporter(path);

	@Test
	public void searchTest() {
		StorageManager.registerExporter(dataHandlerSpecification);
		Map<String, Object> exactMap = new HashMap<>();
		Map<String, Object> sameStartMap = new HashMap<>();
		Map<String, Object> exactNestedMap = new HashMap<>();
		Map<String, Object> sameStartNestedMap = new HashMap<>();
		String nestedEntityName = "";
		List<Entity> entities;

		exactMap.put("id", "1");
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertEquals(1, entities.size());
		assertEquals(1, entities.get(0).getId());

		sameStartMap.put("id", "1");
		exactMap.clear();
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertEquals(10, entities.size());
		assertEquals(1, entities.get(0).getId());
		assertEquals(10, entities.get(1).getId());

		exactMap.put("id", "1");
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertEquals(1, entities.size());
		assertEquals(1, entities.get(0).getId());

		nestedEntityName = "nestedEntity";
		exactMap.clear();
		sameStartMap.clear();
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertEquals(2, entities.size());
		assertEquals(1, entities.get(0).getId());
		assertEquals(15, entities.get(1).getId());

		sameStartMap.put("id", "1");
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertFalse(entities.isEmpty());
		assertEquals(2, entities.size());
		assertEquals(1, entities.get(0).getId());
		assertEquals(15, entities.get(1).getId());

		exactNestedMap.put("id", "2");
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertFalse(entities.isEmpty());
		assertEquals(1, entities.size());
		assertEquals(1, entities.get(0).getId());

		exactNestedMap.clear();
		sameStartNestedMap.put("name", "nested");
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertEquals(2, entities.size());
		assertEquals(1, entities.get(0).getId());
		assertEquals(15, entities.get(1).getId());

		sameStartNestedMap.put("id", "1");
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertEquals(1, entities.size());
		assertEquals(15, entities.get(0).getId());

		exactNestedMap.put("id", "2");
		sameStartNestedMap.clear();
		sameStartNestedMap.put("name", "nested");
		entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
		assertEquals(1, entities.size());
		assertEquals(1, entities.get(0).getId());
	}

	@Test
	public void createNewEntityTest() {
		StorageManager.registerExporter(dataHandlerSpecification);

		int id;
		String name;
		Map<String, Object> propertyMap = new HashMap<>();
		Map<String, Object> nestedPropertyMap = new HashMap<>();
		String nestedName = "";
		List<Entity> entities;

		name = "Matija";
		entities = dataHandlerSpecification.getFileService().read();
		int size = entities.size();
		dataHandlerSpecification.create(name);
		entities = dataHandlerSpecification.getFileService().read();
		assertEquals(size+1, entities.size());

		id = 2;
		assertFalse(dataHandlerSpecification.create(id, name));

		id = 200;
		assertFalse(dataHandlerSpecification.create(id, name));

		propertyMap.put("major", "RN");
		entities = dataHandlerSpecification.getFileService().read();
		size = entities.size();
		dataHandlerSpecification.create(name, propertyMap);
		entities = dataHandlerSpecification.getFileService().read();
		assertEquals(size+1, entities.size());

		id = 202;
		assertFalse(dataHandlerSpecification.create(id, name, propertyMap));

		nestedName = "nested";
		nestedPropertyMap.put("mom", "Vesna");
		dataHandlerSpecification.create(name, propertyMap, nestedName, nestedPropertyMap);
	}
}
