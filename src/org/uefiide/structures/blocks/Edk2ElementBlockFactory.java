package org.uefiide.structures.blocks;

public class Edk2ElementBlockFactory {
	public static Edk2ElementBlock createElementBlock(String sectionName) {
		Edk2ElementBlock block = null;
		
		switch(sectionName.toLowerCase()) {
		case "[defines]":
			block = new DefinitionsBlock();
			break;
		case "[sources]":
			block = new SourcesBlock();
			break;
		case "[packages]":
			block = new PackagesBlock();
			break;
		case "[libraryclasses]":
			block = new LibraryClassesBlock();
			break;
		default:
			block = new UnknownBlock();
			break;
		}
		
		return block;
	}
}
