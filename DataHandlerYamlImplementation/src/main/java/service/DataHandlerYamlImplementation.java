package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import model.Entity;
import repository.FileRepository;
import spec.DataHandlerSpecification;
import spec.StorageManager;

/** @noinspection unchecked*/
public class DataHandlerYamlImplementation extends DataHandlerSpecification {
	
	private EntityService entityService;

	public DataHandlerYamlImplementation() {
		fileService = new FileRepository();
		entityService = new EntityService(fileService);
	}
	
	static {
		StorageManager.registerExporter(new DataHandlerYamlImplementation());
	}

	@Override
	public boolean createEntity(Integer id, String name, Map<String, Object> propertyMap, String nestedName , Map<String, Object> nestedPropertyMap) {
		Entity entity = new Entity();

		if (!entityService.setId(id, entity)) return false;

		if (name != null) entity.setProperty("name", name);
		if (propertyMap != null) entity.setPropertyMap(propertyMap);
		if (nestedName != null) {
			Entity nestedEntity = new Entity();
			nestedEntity.setPropertyMap(nestedPropertyMap);
			entity.setProperty(name, nestedEntity);
		}

		fileService.write(entity);
		return true;
	}

	@Override
	public void create(String name) {
		createEntity(null, name, null, null, null);
	}

	@Override
	public void create(String name, Map<String, Object> propertyMap) {
		createEntity(null, name, propertyMap, null, null);
	}

	@Override
	public boolean create(Integer id, String name) {
		return createEntity(id, name, null, null, null);
	}

	@Override
	public boolean create(Integer id, String name, Map<String, Object> propertyMap) {
		return createEntity(id, name, propertyMap, null, null);
	}

	@Override
	public void create(String name, String nestedName, Map<String, Object> nestedPropertyMap) {
		createEntity(null, name, null, nestedName, nestedPropertyMap);
	}

	@Override
	public boolean create(Integer id, String name, String nestedName, Map<String, Object> nestedPropertyMap) {
		return createEntity(id, name, null, nestedName, nestedPropertyMap);
	}

	@Override
	public void create(String name, Map<String, Object> propertyMap, String nestedName, Map<String, Object> nestedPropertyMap) {
		createEntity(null, name, propertyMap, nestedName, nestedPropertyMap);
	}

	@Override
	public boolean create(Integer id, String name, Map<String, Object> propertyMap, String nestedName, Map<String, Object> nestedPropertyMap) {
		return createEntity(id, name, propertyMap, nestedName, nestedPropertyMap);
	}

	@Override
	public boolean createNested(Integer id, String nestedName, Map<String, Object> nestedPropertyMap) {
		File[] files = new File(directoryName).listFiles();
		String path = null;
		List<Entity> entities = new ArrayList<>();

		if (files == null) return false;
		for (File file : files) {
			if (file.isDirectory())
				continue;

			Entity entity = entityService.isExisting(id, file, entities);
			if (entity == null)
				continue;

			Entity nestedEntity = new Entity();
			if (!entityService.setId((Integer) nestedPropertyMap.get("id"), nestedEntity))
				return false;

			nestedEntity.getPropertyMap().putAll(nestedPropertyMap);
			entity.setProperty(nestedName, nestedEntity);

			path = file.getAbsolutePath();
	    }
		if (path == null) return false;

		fileService.clearFile(path);
		fileService.write(entities);

		return true;
	}

	@Override
	public List<Entity> search(Map<String, Object> exactProperties, Map<String, Object> sameStartProperties, String nestedEntityName,
							   Map<String, Object> exactNestedProperties, Map<String, Object> sameStartNestedProperties) {
		List<Entity> entities = fileService.read();

		entities.iterator().forEachRemaining(entity -> {
			if (entityService.checkPropertyCriteria(exactProperties, sameStartProperties, entity))
				entities.remove(entity);

			if (!entity.getPropertyMap().containsKey(nestedEntityName)) {
				entities.remove(entity);
				return;
			}

			Entity nestedEntity = (Entity) entity.getPropertyMap().get(nestedEntityName);
			if (entityService.checkPropertyCriteria(exactNestedProperties, sameStartNestedProperties, nestedEntity))
				entities.remove(entity);
		});

		return entities;
	}


	
	@Override
	public boolean delete(Integer id) {
		List<Entity> entities = fileService.read();
		Entity entity = null;

		for (Entity e : entities)
			if (e.getId().equals(id)) {
				entity = e;
				break;
			}

		if (entity != null) entities.remove(entity);
		else return false;

		fileService.clearFiles();
		fileService.write(entities);

		return true;
	}

	@Override
	public int delete(Map<String, Object> propertyMap) {
		int counter = 0;
		List<Entity> entities = fileService.read();

		entities.iterator().forEachRemaining(entity -> {
			if (entityService.arePropertiesEqual(propertyMap, entity.getPropertyMap()))
				entities.remove(entity);
		});

		fileService.clearFiles();
		fileService.write(entities);

		return counter;
	}

}
