package service;

import model.Entity;
import spec.FileServiceSpecification;
import java.io.File;
import java.util.List;
import java.util.Map;

public class EntityService {
    private FileServiceSpecification fileService;

    public EntityService(FileServiceSpecification fileService) {
        this.fileService = fileService;
    }

    boolean checkPropertyCriteria(Map<String, Object> exactProperties, Map<String, Object> sameStartProperties, Entity entity) {
        return !arePropertiesEqual(exactProperties, entity.getPropertyMap()) ||
                !doPropertiesStartTheSame(sameStartProperties, entity.getPropertyMap());
    }

    private boolean doPropertiesStartTheSame(Map<String, Object> propertyMap, Map<String, Object> entityPropertyMap) {
        if (propertyMap.isEmpty())
            return true;
        if (entityPropertyMap.isEmpty())
            return false;

        for (Map.Entry<String, Object> property : propertyMap.entrySet()) {
            for (Map.Entry<String, Object> entityProperty : entityPropertyMap.entrySet()) {
                if (!property.getKey().equals(entityProperty.getKey()) ||
                        !property.getValue().toString().startsWith(entityProperty.getValue().toString()))
                    return false;
            }
        }

        return true;
    }

    boolean arePropertiesEqual(Map<String, Object> propertyMap, Map<String, Object> entityPropertyMap) {
        if (propertyMap.isEmpty())
            return true;
        if (entityPropertyMap.isEmpty())
            return false;

        for (Map.Entry<String, Object> property : propertyMap.entrySet()) {
            for (Map.Entry<String, Object> entityProperty : entityPropertyMap.entrySet()) {
                if (!property.getKey().equals(entityProperty.getKey())) {
                    if (!property.getValue().equals(entityProperty.getValue()))
                        return false;
                }
            }
        }

        return true;
    }

    Entity isExisting(Integer id, File file, List<Entity> entities) {
        entities.addAll(fileService.importData(file.getAbsolutePath()));

        for(Entity entity : entities) {
            if (entity.getId().equals(id))
                return entity;
        }

        return null;
    }

    private Entity isExisting(Integer id) {
        List<Entity> entities = fileService.read();

        for(Entity entity : entities) {
            if (entity.getId().equals(id))
                return entity;
        }

        return null;
    }

    private Integer findFreeId() {
        List<Entity> entities = fileService.read();

        int freeId = 0;
        int found;
        while(true) {
            found = 1;
            for (Entity entity : entities) {
                if (entity.getId() == freeId) {
                    found = 0;
                    break;
                }
            }

            if (found == 1) break;
            freeId++;
        }

        return freeId;
    }

    boolean setId(Integer id, Entity entity) {
        if (id != null) {
            if (isExisting(id) != null) return false;
        }
        else id = findFreeId();

        entity.setProperty("id", id);
        return true;
    }

}
