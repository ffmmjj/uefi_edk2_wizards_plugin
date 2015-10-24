package org.uefiide.structures.blocks;

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
}
