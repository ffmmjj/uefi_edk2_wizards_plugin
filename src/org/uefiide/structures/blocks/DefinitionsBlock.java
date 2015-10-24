package org.uefiide.structures.blocks;

import java.util.HashMap;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DefinitionsBlock implements Edk2ElementBlock {
	private Map<String, String> defines = new HashMap<String, String>();
	
	public DefinitionsBlock() {
		
	}
	
	public String getDefinitionValue(String key) {
		return this.defines.get(key);
	}
	
	public void addDefinition(String key, String value) {
		this.defines.put(key, value);
	}
	
	public void removeDefinition(String key) {
		this.defines.remove(key);
	}
	
	@Override
	public String toString() {
		throw new NotImplementedException();
	}

	@Override
	public void addLine(String line) {
		String[] keyValuePair = line.split("=");
		if(keyValuePair.length != 2) {
			throw new IllegalArgumentException("Could not extract a key-value pair from the line \"" + line + "\"");
		}
		
		this.addDefinition(keyValuePair[0].trim(), keyValuePair[1].trim());
	}
}
