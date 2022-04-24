package model;

import java.util.Comparator;

public class EntityComparatorDescendingById implements Comparator<Entity> {

	@Override
	public int compare(Entity o1, Entity o2) {
		return o2.getId().compareTo(o1.getId());
	}

}
