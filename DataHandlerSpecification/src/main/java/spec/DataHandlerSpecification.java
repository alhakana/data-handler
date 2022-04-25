package spec;

import java.util.List;
import java.util.Map;
import model.Entity;
import service.FileServiceSpecification;

/**
 * Class which contains abstract methods.
 */
public abstract class DataHandlerSpecification {

	protected FileServiceSpecification fileService;
	/**
	 * Path to directory which contains files written in different formats.
	 */
	public static String directoryName;

	/**
	 * The biggest number of entities which can be written to file.
	 */
	public static Integer entityPerFile = 50;

	/**
	 * Setter for the path to the directory which contains fiels.
	 */
	void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	


	public abstract boolean createEntity(Integer id, String name, Map<String, Object> propertyMap, String nestedName , Map<String, Object> nestedPropertyMap);

	/**
	 * Method which creates entity with parameter name and auto-generated ID.
	 * @param name name of the entity
	 */
	public abstract void create(String name);

	/**
	 * Method which creates entity with name and ID.
	 * @param id ID of the entity
	 * @param name name of the entity
	 * @return returns true if there is no entity with passed ID.
	 */
	public abstract boolean create(Integer id, String name);

	/**
	 * Method which creates entity.
	 * @param name name of the entity
	 * @param properties properties [name -> value]
	 */
	public abstract void create(String name, Map<String, Object> properties);

	/**
	 * Method which creates entity.
	 * @param id ID of the entity
	 * @param name name of the entity
	 * @param properties properties [property_name -> property_value]
	 * @return returns true if there is no entity with passed ID.
	 */
	public abstract boolean create(Integer id, String name, Map<String, Object> properties);

	/**
	 * Method which creates entity with nested entity.
	 * @param name name of the enity
	 * @param nastedName name of the nested entity
	 * @param nestedProperties properties of the nested entity [property_name -> property_value]
	 */
	public abstract void create(String name, String nastedName, Map<String, Object> nestedProperties);

	/**
	 * Metoda koja kreira entitet sa prosledjenim imenom i ID-em koji ima ugnjezdeni entitet sa imenom 
	 * nestedName i propertijima iz nestedproperties.
	 * @param id ID entiteta
	 * @param name naziv glavnog entiteta
	 * @param nestedName naziv ugnjezdenog entiteta
	 * @param nestedproperties propertiji ugnjezdenog entiteta u formatu: kljuc-naziv, vrednost-vrednost
	 * @return vraca true ako ne postoji entitet sa ovim ID-em
	 */
	public abstract boolean create(Integer id, String name, String nestedName, Map<String, Object> nestedproperties);
	/**
	 * Metoda koja kreira entitet sa prosledjenim imenom, automatski generisanem ID-em i  propertijima koji su prosledjeni u mapi, koji ima ugnjezdeni entitet sa imenom 
	 * nestedName i propertijima iz nestedproperties.
	 * @param name naziv entiteta
	 * @param properties propertiji u formatu: kljuc-naziv, vrednost-vrednost
	 * @param nestedName naziv ugnjezdenog entiteta
	 * @param nestedproperties propertiji ugnjezdenog entiteta u formatu: kljuc-naziv, vrednost-vrednost
	 */
	public abstract void create(String name, Map<String, Object> properties, String nestedName, Map<String, Object> nestedproperties);
	/**
	 * Metoda koja kreira entitet sa prosledjenim imenom, ID-em i  propertijima koji su prosledjeni u mapi, koji ima ugnjezdeni entitet sa imenom 
	 * nestedName i propertijima iz nestedproperties.
	 * @param id ID entiteta
	 * @param name naziv entiteta
	 * @param properties propertiji u formatu: kljuc-naziv, vrednost-vrednost
	 * @param nestedName naziv ugnjezdenog entiteta
	 * @param nestedPropertyMap propertiji ugnjezdenog entiteta u formatu: kljuc-naziv, vrednost-vrednost
	 * @return vraca true ako ne postoji entitet sa ovim ID-em
	 */
	public abstract boolean create(Integer id, String name, Map<String, Object> properties, String nestedName, Map<String, Object> nestedPropertyMap);
	/**
	 * Metoda koja ugnjezdava entitet u vec postojeci.
	 * @param id ID postojeceg entiteta
	 * @param nestedName naziv ugnjezdenog entiteta
	 * @param nestedPropertyMap propertiji u formatu: kljuc-naziv, vrednost-vrednost
	 * @return vraca false ako ne postoji entitet sa ovim ID-em
	 */
	public abstract boolean createNested(Integer id, String nestedName, Map<String, Object> nestedPropertyMap);
	
	/**
	 * Methos which search for the entity with passed parameters.
	 * @param exactProperties
	 * @param sameStartProperties
	 * @param nestedEntityName
	 * @param exactNestedProperties
	 * @param sameStartNestedProperties
	 * @return
	 */
	public abstract List<Entity> search(Map<String, Object> exactProperties, Map<String, Object> sameStartProperties,
										String nestedEntityName, Map<String, Object> exactNestedProperties, Map<String, Object> sameStartNestedProperties);
	
	/**
	 * Method which deletes entity.
	 * @param id ID of the entity
	 * @return returns true if entity with ID id exists
	 */
	public abstract boolean delete(Integer id);
	
	
	/**
	 * Method which deletes entity which has passed parameters
	 * @param properties properties of the entity [property_name -> property-value]
	 * @return returns true if entity with passed parameters exists.
	 */
	public abstract int delete(Map<String, Object> properties);

	public FileServiceSpecification getFileService() {
		return fileService;
	}
}
