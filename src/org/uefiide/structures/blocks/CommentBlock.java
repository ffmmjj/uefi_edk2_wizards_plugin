package org.uefiide.structures.blocks;

import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;

public class CommentBlock implements Edk2ElementBlock{
	public String line;
	
	public CommentBlock() {
		
	}
	
	@Override
	public void addLine(String commentLine) {
		this.line = commentLine;
	}
	
	@Override
	public String toString() {
		return this.line;
	}

	@Override
	public void accept(Edk2ElementBlockVisitor visitor) {
		
	}
}
