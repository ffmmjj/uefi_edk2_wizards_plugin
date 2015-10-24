package org.uefiide.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LibraryClassesBlock implements Edk2ElementBlock {
	public List<String> libraryClasses = new LinkedList<String>();
	
	public LibraryClassesBlock() {
		
	}
	
	public void addLibraryClass(String libraryClassName) {
		this.libraryClasses.add(libraryClassName);
	}
	
	public List<String> getLibraryClasses() {
		return this.libraryClasses;
	}
	
	@Override
	public String toString() {
		throw new NotImplementedException();
	}
}
