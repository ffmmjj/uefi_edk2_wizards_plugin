package org.uefiide.structures.blocks;

public class Edk2ElementBlockFactory {
	public static Edk2ElementBlock createElementBlock(String sectionName) {
		Edk2ElementBlock block = null;
		
		switch(sectionName.toLowerCase()) {
		case "[defines]":
			block = new DefinitionsBlock();
			break;
			
		case "[sources]":
		case "[sources.common]":
		case "[sources.x64]":
			block = new SourcesBlock();
			break;
			
		case "[packages]":
		case "[packages.common]":
		case "[packages.x64]":
			block = new PackagesBlock();
			break;
			
		case "[libraryclasses]":
		case "[libraryclasses.common]":
		case "[libraryclasses.x64]":
			block = new LibraryClassesBlock();
			break;
			
		default:
			block = new UnknownBlock();
			break;
		}
		
		return block;
	}
}
