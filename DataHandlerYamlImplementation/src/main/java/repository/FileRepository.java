package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import model.Entity;
import service.DataHandlerYamlImplementation;
import service.FileServiceSpecification;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** @noinspection unchecked, ResultOfMethodCallIgnored */
public class FileRepository extends FileServiceSpecification {

    @Override
    public List<Entity> read() {
        List<Entity> entities = new ArrayList<>();

        File[] files = new File(DataHandlerYamlImplementation.directoryName).listFiles();

        if (files != null)
            Arrays.stream(files).iterator().forEachRemaining(file -> entities.addAll(importData(file.getAbsolutePath())));

        return entities;
    }

    @Override
    public List<Entity> importData(String file) {
        List<Entity> entities = new ArrayList<>();

        if (new File(file).length() == 0)
            return entities;

        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.findAndRegisterModules();
            Map<String, Object>[] content = objectMapper.readValue(new File(file), Map[].class);

            for (Map<String, Object> properties : content) {
                Entity entity = new Entity();
                readEntities(entity, properties);

                entities.add(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entities;
    }

    private void readEntities(Entity entity, Map<String, Object> properties) {
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            if (property.getValue() instanceof Map) {
                Entity nestedEntity = new Entity();
                readEntities(nestedEntity, (Map<String, Object>)property.getValue());
                entity.setProperty(property.getKey(), nestedEntity);
            } else {
                readProperty(entity, property);
            }
        }
    }

    private void readProperty(Entity entity, Map.Entry<String, Object> property) {
        entity.setProperty(property.getKey(), property.getValue());
    }


    @Override
    public void write(Entity newEntity) {
        try {
            List<Entity> entities = new ArrayList<>();
            String path = null;
            File[] files = new File(DataHandlerYamlImplementation.directoryName).listFiles();

            if (files != null) path = findAvailableFile(files, entities);
            if (path == null) path = makeNewFile();

            entities.add(newEntity);

            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write("");
            for (Entity entity : entities)
                formatEntity(entity, fileWriter, 2);

            fileWriter.close();

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void formatEntity(Entity entity, FileWriter fileWriter, int level) throws IOException {
        if (level == 2) {
            fileWriter.append("- ");
        } else appendSpaces(fileWriter, level);

        fileWriter.append("id: ").append(String.valueOf(entity.getId())).append("\n");

        if (entity.getPropertyMap() != null) {
            for (Map.Entry<String, Object> property : entity.getPropertyMap().entrySet()) {
                if(property.getKey().equals("id"))
                    continue;
                appendSpaces(fileWriter, level);

                if (property.getValue() instanceof Entity) {
                    fileWriter.append(property.getKey()).append(": ").append("\n");
                    formatEntity((Entity) property.getValue(), fileWriter, level+1);
                    continue;
                }

                fileWriter.append(property.getKey()).append(": ").append(String.valueOf(property.getValue())).append("\n");
            }
        }
    }

    private void appendSpaces(FileWriter fileWriter, int level) throws IOException {
        for(int i = 0; i < level-1; i++) {
            fileWriter.append(" ");
        }
        fileWriter.append(" ");
    }

    private String makeNewFile() {
        int fileCounter = 0;
        String path = DataHandlerYamlImplementation.directoryName + "\\YamlTest" + fileCounter;
        File file = new File(path);
        while (file.exists()) {
            fileCounter++;
            path = DataHandlerYamlImplementation.directoryName + "\\YamlTest" + fileCounter;
            file = new File(path);
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    private String findAvailableFile(File[] files, List<Entity> entities) {
        for (File file : files) {
            if (file.isDirectory() || file.length() == 0)
                continue;

            entities.addAll(importData(file.getAbsolutePath()));

            if (entities.size() < DataHandlerYamlImplementation.entityPerFile-1)
                return file.getAbsolutePath();
        }

        return null;
    }

    @Override
    public void write(List<Entity> entities) {
        entities.iterator().forEachRemaining(this::write);
    }

    public void clearFiles() {
        File[] files = new File(DataHandlerYamlImplementation.directoryName).listFiles();

        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory() || file.length() == 0)
                continue;

            clearFile(file.getAbsolutePath());
        }
    }


    public void clearFile(String path) {
        try {
            FileWriter fw = new FileWriter(path);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
