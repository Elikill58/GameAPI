package com.elikill58.api.inventories;

public class InventoryType {
	
	private final String id;
	
	public InventoryType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof InventoryType))
			return false;
		return this.id.equalsIgnoreCase(((InventoryType) obj).id) || super.equals(obj);
	}
}
