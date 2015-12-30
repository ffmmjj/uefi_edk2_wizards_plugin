package org.uefiide.structures.blocks.visitors;

import java.util.List;
import java.util.Map;

import org.uefiide.structures.Edk2Module.Edk2ModuleType;
import org.uefiide.structures.blocks.DefinitionsBlock;
import org.uefiide.structures.blocks.Edk2ElementBlockType;
import org.uefiide.structures.blocks.LibraryClassesBlock;
import org.uefiide.structures.blocks.PackagesBlock;
import org.uefiide.structures.blocks.SourcesBlock;

public class BlockFinderVisitor implements Edk2ElementBlockVisitor {

	Edk2ElementBlockType type;
	boolean found;
	
	public BlockFinderVisitor(Edk2ElementBlockType type) {
		this.type = type;
		this.found = false;
	}

	@Override
	public void visit(DefinitionsBlock defs) {
		this.found |= this.type == Edk2ElementBlockType.DEFINITIONS_BLOCK;
	}

	@Override
	public void visit(SourcesBlock sources) {
		this.found |= this.type == Edk2ElementBlockType.SOURCES_BLOCK;
	}

	@Override
	public void visit(PackagesBlock packages) {
		this.found |= this.type == Edk2ElementBlockType.PACKAGES_BLOCK;		
	}

	@Override
	public void visit(LibraryClassesBlock libs) {
		this.found |= this.type == Edk2ElementBlockType.LIBRARY_CLASSES_BLOCK;
	}
	
	public boolean foundBlock() {
		return this.found;
	}

}
