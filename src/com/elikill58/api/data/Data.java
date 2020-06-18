package com.elikill58.api.data;

public class Data {
	
	private final String key, databaseType, defaultValue;
	private final boolean canBeNull;
	
	/**
	 * Create a data that will be used in data
	 * 
	 * @param key of the data (which will be saved)
	 * @param databaseType used for database
	 * @param defaultValue the default value of the data
	 */
	public Data(String key, String databaseType, String defaultValue) {
		this(key, databaseType, defaultValue, false);
	}

	/**
	 * Create a data that will be used in data
	 * 
	 * @param key of the data (which will be saved)
	 * @param databaseType used for database
	 * @param defaultValue the default value of the data
	 * @param canBeNull
	 */
	public Data(String key, String databaseType, String defaultValue, boolean canBeNull) {
		this.key = key;
		this.databaseType = databaseType;
		this.defaultValue = defaultValue;
		this.canBeNull = canBeNull;
	}

	public String getKey() {
		return key;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public boolean isCanBeNull() {
		return canBeNull;
	}
	
	public String toDB() {
		String msg = "`" + getKey() + "` " + getDatabaseType();
		if(defaultValue != null && !defaultValue.equalsIgnoreCase(""))
			msg += " DEFAULT " + defaultValue;
		if(!isCanBeNull())
			msg += " NOT NULL";
		return msg;
	}
}
