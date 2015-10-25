package org.uefiide.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UnknownBlock implements Edk2ElementBlock {
	public List<String> lines = new LinkedList<String>();
	
	public UnknownBlock() {
		
	}
	
	
	
	public List<String> getLines() {
		return this.lines;
	}
	
	@Override
	public String toString() {
		throw new NotImplementedException();
	}
	
	@Override
	public void addLine(String sourceFileName) {
		this.lines.add(sourceFileName);
	}

	@Override
	public void accept(Edk2ElementBlockVisitor visitor) {
	}
}
