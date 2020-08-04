package com.elikill58.api.data;

import javax.annotation.Nullable;

public class Data {
	
	private final String key, databaseType, defaultValue;
	private final int dataVersion;
	private final boolean canBeNull;
	
	/**
	 * Create a data that will be used in data
	 * 
	 * @param key of the data (which will be saved)
	 * @param databaseType used for database
	 * @param defaultValue the default value of the data
	 * @param dataVersion the version of the data (0 for first version)
	 */
	public Data(String key, String databaseType, String defaultValue, int dataVersion) {
		this(key, databaseType, defaultValue, dataVersion, false);
	}

	/**
	 * Create a data that will be used in data
	 * 
	 * @param key of the data (which will be saved)
	 * @param databaseType used for database
	 * @param defaultValue the default value of the data
	 * @param dataVersion the version of the data (0 for first version)
	 * @param canBeNull true if the data can be null
	 */
	public Data(String key, String databaseType, String defaultValue, int dataVersion, boolean canBeNull) {
		this.key = key;
		this.databaseType = databaseType;
		this.defaultValue = defaultValue;
		this.dataVersion = dataVersion;
		this.canBeNull = canBeNull;
	}

	/**
	 * Get the data key, used to save data
	 * 
	 * @return the key of the data
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Type of the data, and so the needed type of the column
	 * (Use for database)
	 * 
	 * @return the type for database
	 */
	@Nullable
	public String getDatabaseType() {
		return databaseType;
	}

	/**
	 * The default value of this data
	 * 
	 * @return the default value
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Check if the data can be null or not
	 * 
	 * @return true if the data can be null
	 */
	public boolean isCanBeNull() {
		return canBeNull;
	}
	
	/**
	 * Get the version ID when this data have been added
	 * 
	 * @return the version of data
	 */
	public int getDataVersion() {
		return dataVersion;
	}
	
	/**
	 * Convert this data to a part of a database script
	 * 
	 * @return the database script
	 */
	public String toDB() {
		String msg = "`" + getKey() + "` " + getDatabaseType();
		if(defaultValue != null && !defaultValue.equalsIgnoreCase(""))
			msg += " DEFAULT " + defaultValue;
		if(!isCanBeNull())
			msg += " NOT NULL";
		return msg;
	}
}
