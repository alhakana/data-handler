package model;

import java.util.Comparator;

public class EntityComparatorDescendingByName implements Comparator<Entity> {

	@Override
	public int compare(Entity o1, Entity o2) {
		return o2.getName().toLowerCase().compareTo(o1.getName().toLowerCase());
	}

}
