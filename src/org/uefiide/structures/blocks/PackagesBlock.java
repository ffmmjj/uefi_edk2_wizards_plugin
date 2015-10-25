package org.uefiide.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PackagesBlock implements Edk2ElementBlock {
	public List<String> packages = new LinkedList<String>();
	
	public PackagesBlock() {
		
	}
	
	public void addPackage(String sourceFileName) {
		this.packages.add(sourceFileName);
	}
	
	public List<String> getPackages() {
		return this.packages;
	}
	
	@Override
	public String toString() {
		throw new NotImplementedException();
	}

	@Override
	public void addLine(String line) {
		this.addPackage(line);
	}

	@Override
	public void accept(Edk2ElementBlockVisitor visitor) {
		visitor.visit(this);
	}
}
