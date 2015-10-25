package org.uefiide.structures.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DefinitionsBlock implements Edk2ElementBlock {
	private Map<String, String> defines = new HashMap<String, String>();
	
	public DefinitionsBlock() {
		
	}
	
	public String getDefinitionValue(String key) {
		return this.defines.get(key);
	}
	
	public void setDefinitions(Map<String, String> definitions) {
		this.defines = definitions;
	}
	
	public void addDefinition(String key, String value) {
		this.defines.put(key, value);
	}
	
	public void removeDefinition(String key) {
		this.defines.remove(key);
	}
	
	public Map<String, String> entries() {
		return this.defines;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[Defines]" + System.lineSeparator());
		for(Entry<String, String> entry : this.defines.entrySet()) {
			buffer.append(entry.getKey() + " = " + entry.getValue() + System.lineSeparator());
		}
		
		return buffer.toString();
	}

	@Override
	public void addLine(String line) {
		String[] keyValuePair = line.split("=");
		if(keyValuePair.length != 2) {
			throw new IllegalArgumentException("Could not extract a key-value pair from the line \"" + line + "\"");
		}
		
		this.addDefinition(keyValuePair[0].trim(), keyValuePair[1].trim());
	}

	@Override
	public void accept(Edk2ElementBlockVisitor visitor) {
		visitor.visit(this);
	}
}
