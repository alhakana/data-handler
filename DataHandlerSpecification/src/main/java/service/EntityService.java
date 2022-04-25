package service;

import model.Entity;

import java.io.File;
import java.util.List;
import java.util.Map;

public class EntityService {

    private final FileServiceSpecification fileService;

    public EntityService(FileServiceSpecification fileService) {
        this.fileService = fileService;
    }

    boolean checkPropertyCriteria(Map<String, Object> exactProperties, Map<String, Object> sameStartProperties, Entity entity) {
        return arePropertiesEqual(exactProperties, entity.getPropertyMap(), entity.getId()) &&
                doPropertiesStartTheSame(sameStartProperties, entity.getPropertyMap());
    }

    private boolean doPropertiesStartTheSame(Map<String, Object> propertyMap, Map<String, Object> entityPropertyMap) {
        if (propertyMap.isEmpty())
            return true;
        if (entityPropertyMap.isEmpty())
            return false;

        for (Map.Entry<String, Object> property : propertyMap.entrySet()) {
            if (property.getKey().equals("id")) {
                if (!entityPropertyMap.get("id").toString().startsWith(property.getValue().toString()))
                    return false;
            }
            if (!entityPropertyMap.containsKey(property.getKey()))
                return false;
            if (!entityPropertyMap.get(property.getKey()).toString().startsWith(property.getValue().toString()))
                return false;
        }

        return true;
    }

    boolean arePropertiesEqual(Map<String, Object> propertyMap, Map<String, Object> entityPropertyMap, Integer id) {
        if (propertyMap.isEmpty())
            return true;
        if (entityPropertyMap.isEmpty())
            return false;

        for (Map.Entry<String, Object> property : propertyMap.entrySet()) {
            if (property.getKey().equals("id")) {
                if (Integer.parseInt((String) property.getValue()) != id)
                    return false;
            } else if (!property.getValue().equals(entityPropertyMap.get(property.getKey())))
                return false;
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

    private Integer findFreeId() {
        List<Entity> entities = fileService.read();

        int freeId = 0;
        int found;
        while(true) {
            found = 1;
            for (Entity entity : entities) {
                if (!isIdFree(freeId, entity)) {
                    found = 0;
                    break;
                }
            }

            if (found == 1) break;
            freeId++;
        }

        return freeId;
    }

    private Integer findFreeId(Integer parentId) {
        List<Entity> entities = fileService.read();

        int freeId = 0;
        int found;
        while(true) {
            if (parentId == freeId) {
                freeId++;
                continue;
            }
            found = 1;
            for (Entity entity : entities) {
                if (!isIdFree(freeId, entity)) {
                    found = 0;
                    break;
                }
            }

            if (found == 1) break;
            freeId++;
        }

        return freeId;
    }

    private boolean isIdFree(Integer id, Entity entity) {
        if (entity.getId().equals(id))
            return false;
        for (Map.Entry<String, Object> property : entity.getPropertyMap().entrySet()) {
            if (!(property.getValue() instanceof Entity))
                continue;

            return isIdFree(id, (Entity) property.getValue());
        }
        return true;
    }

    public boolean isExisting(Integer id) {
        List<Entity> entities = fileService.read();

        for (Entity entity : entities) {
            if (!isIdFree(id, entity)) {
                return true;
            }
        }

        return false;
    }

    public boolean isExisting(Integer id, Integer parentId) {
        List<Entity> entities = fileService.read();

        for (Entity entity : entities) {
            if (!id.equals(parentId) && !isIdFree(id, entity)) {
                return true;
            }
        }

        return false;
    }

    boolean setId(Integer id, Entity entity) {
        if (id != null) {
            if (isExisting(id)) return false;
        }
        else id = findFreeId();

        entity.setProperty("id", id);
        return true;
    }

    boolean setId(Integer id, Integer parentId, Entity entity) {
        if (id != null) {
            if (isExisting(id, parentId)) return false;
        }
        else id = findFreeId(parentId);

        entity.setProperty("id", id);
        return true;
    }

}
