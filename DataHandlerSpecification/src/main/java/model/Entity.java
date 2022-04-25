package model;

import java.util.HashMap;
import java.util.Map;

public class Entity implements Comparable<Entity> {

	/**
	 * Entity ID - can be auto-generated.
	 */
	private Integer id;

	/**
	 * Map of the entity properties, including nested entities [property_name -> property_value] or [entity_name -> entity]
	 */
	private Map<String, Object> propertyMap;

	public Entity() {
		propertyMap = new HashMap<String, Object>();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("ID: ").append(id).append("\n");

		for(Map.Entry<String, Object> property : propertyMap.entrySet()) {
			if (property.getKey().equals("id"))
				continue;
			if (property.getValue() instanceof Entity) {
				stringBuilder.append("\n");
			}
			stringBuilder.append(property.getKey()).append(" = ").append(property.getValue()).append("\n");
		}

		return stringBuilder.toString();
	}

	/**
	 * Two entities are equal if they have the same ID.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Entity) {
			Entity entity = (Entity)obj;
			return entity.id.equals(id);
		}
		return false;
	}

	public void setProperty(String name, Object value) {
		if (name.equals("id")) {
			if (value instanceof String)
				id = Integer.parseInt((String) value);
			else id = (Integer) value;
		}
		propertyMap.put(name, value);
	}

	public int compareTo(Entity entity) {
		return id.compareTo(entity.id);
	}

	public Integer getId() {
		return id;
	}

	public Map<String, Object> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, Object> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public String getName() {
		if (propertyMap.containsKey("name"))
			return propertyMap.get("name").toString();
		return "";
	}

	public String getProperties() {
		StringBuilder stringBuilder = new StringBuilder();

		for(Map.Entry<String, Object> property : propertyMap.entrySet()) {
			if (property.getKey().equals("id") || property.getKey().equals("name") ||
					property.getValue() instanceof Entity)
				continue;
			stringBuilder.append(property.getKey()).append(" = ").append(property.getValue()).append("\n");
		}

		return stringBuilder.toString();
	}

	public String getEntities() {
		StringBuilder stringBuilder = new StringBuilder();

		for(Map.Entry<String, Object> property : propertyMap.entrySet()) {
			if (property.getValue() instanceof Entity) {
				stringBuilder.append(property.getKey()).append("\n");
				stringBuilder.append("id = ").append(((Entity) property.getValue()).getId()).append("\n");
				if (((Entity) property.getValue()).getPropertyMap().containsKey("name"))
					stringBuilder.append("name = ").append(((Entity) property.getValue()).getName()).append("\n");
				stringBuilder.append(((Entity) property.getValue()).getProperties());
			}
		}

		return stringBuilder.toString();
	}

}
