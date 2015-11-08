package org.uefiide.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;

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
	
	public void setPackages(List<String> packages) {
		this.packages = packages;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[Packages]" + System.lineSeparator());
		for(String packageString : this.packages) {
			buffer.append("  " + packageString + System.lineSeparator());
		}
		
		return buffer.toString();
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
