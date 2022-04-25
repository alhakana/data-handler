package repository;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import model.Entity;
import service.DataHandlerJsonImplementation;
import service.FileServiceSpecification;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileRepository extends FileServiceSpecification {

    @Override
    public List<Entity> read() {
        List<Entity> entities = new ArrayList<>();

        File[] files = new File(DataHandlerJsonImplementation.directoryName).listFiles();

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
            JsonReader jsonReader = new JsonReader(new FileReader(file));

            jsonReader.setLenient(true);
            jsonReader.beginArray();

            while(jsonReader.hasNext()) {
                jsonReader.beginObject();

                Entity entity = new Entity();
                readEntities(jsonReader, entity);
                entities.add(entity);

                jsonReader.endObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entities;
    }

    private void readEntities(JsonReader jsonReader, Entity entity) throws IOException {
        if (jsonReader.peek() == JsonToken.END_OBJECT)
            return;

        String propertyName = jsonReader.nextName();

        if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            jsonReader.beginObject();

            Entity nestedEntity = new Entity();
            readEntities(jsonReader, nestedEntity);
            entity.setProperty(propertyName, nestedEntity);

            jsonReader.endObject();
        } else readProperty(jsonReader, entity, propertyName);

        readEntities(jsonReader,entity);
    }

    private void readProperty(JsonReader jsonReader, Entity entity, String propertyName) throws IOException {
        Object propertyValue = jsonReader.nextString();
        entity.setProperty(propertyName, propertyValue);
    }

    private String makeNewFile() {
        int fileCounter = 0;
        String path = DataHandlerJsonImplementation.directoryName + "\\JsonTest" + fileCounter;
        File file = new File(path);
        while (file.exists()) {
            fileCounter++;
            path = DataHandlerJsonImplementation.directoryName + "\\JsonTest" + fileCounter;
            file = new File(path);
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    private void prepareFileForWriting(String path) throws IOException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();

        if (file.length() != 0) {
            FileReader fileReader = new FileReader(file);
            char[] charContent = new char[(int) file.length()];
            fileReader.read(charContent);
            String content = new String(charContent);
            content = content.substring(0, content.indexOf("]"));
            content = content.replace("]", "");
            stringBuilder.append(content);
            stringBuilder.append(",");

            fileReader.close();
        }
        else stringBuilder.append("[");

        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(stringBuilder.toString());
        fileWriter.close();
    }

    public void clearFiles() {
        File[] files = new File(DataHandlerJsonImplementation.directoryName).listFiles();

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

    @Override
    public void write(List<Entity> entities) {
        entities.iterator().forEachRemaining(this::write);
    }

    @Override
    public void write(Entity entity) {
        List<Entity> entities = new ArrayList<>();
        String path = null;
        File[] files = new File(DataHandlerJsonImplementation.directoryName).listFiles();

        if (files != null) path = findAvailableFile(files, entities);
        if (path == null) path = makeNewFile();

        try {
            prepareFileForWriting(path);
            JsonObject object = getJsonObject(entity);
            writeJson(object, path);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void writeJson(JsonObject jsonObject, String path) throws IOException {
        FileWriter fileWriter = new FileWriter(path, true);
        fileWriter.write("\n");
        fileWriter.write(jsonObject.toString());
        fileWriter.write("]");
        fileWriter.close();
    }

    private JsonObject getJsonObject(Entity entity) {
        JsonObject object = new JsonObject();
        object.addProperty("id", entity.getId());

        for(Map.Entry<String, Object> property : entity.getPropertyMap().entrySet()) {
            if (property.getValue() instanceof Entity) {
                object.add(property.getKey(), getJsonObject((Entity) property.getValue()));
                continue;
            }

            object.addProperty(property.getKey(), property.getValue().toString());
        }

        return object;
    }

    private String findAvailableFile(File[] files, List<Entity> entities) {
        for (File file : files) {
            if (file.isDirectory() || file.length() == 0)
                continue;

            entities.addAll(importData(file.getAbsolutePath()));

            if (entities.size() < DataHandlerJsonImplementation.entityPerFile-1)
                return file.getAbsolutePath();
        }

        return null;
    }

}
