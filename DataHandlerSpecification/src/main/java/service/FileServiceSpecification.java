package service;

import model.Entity;

import java.util.List;

public abstract class FileServiceSpecification {

    public abstract List<Entity> importData(String file);

    /**
     * Method which reads entities from the directory.
     * @return list of read entites
     */
    public abstract List<Entity> read();

    /**
     * Method which saves entity in the first free file.
     * @param entity entity that is written
     */
    public abstract void write(Entity entity);

    public abstract void write(List<Entity> entities);

    public abstract void clearFiles();

    public abstract void clearFile(String path);

}
