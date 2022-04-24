package model;

import java.util.Comparator;

public class EntityComparatorAscendingByName implements Comparator<Entity> {

	@Override
	public int compare(Entity o1, Entity o2) {
		return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
	}

}
