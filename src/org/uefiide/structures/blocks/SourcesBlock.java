package org.uefiide.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SourcesBlock implements Edk2ElementBlock {
	public List<String> sourceFiles = new LinkedList<String>();
	
	public SourcesBlock() {
		
	}
	
	public void addSourceFile(String sourceFileName) {
		this.sourceFiles.add(sourceFileName);
	}
	
	public List<String> getSourceFiles() {
		return this.sourceFiles;
	}
	
	@Override
	public String toString() {
		throw new NotImplementedException();
	}
}
