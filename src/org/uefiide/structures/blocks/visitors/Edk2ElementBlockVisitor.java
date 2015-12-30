package org.uefiide.structures.blocks.visitors;

import org.uefiide.structures.blocks.DefinitionsBlock;
import org.uefiide.structures.blocks.LibraryClassesBlock;
import org.uefiide.structures.blocks.PackagesBlock;
import org.uefiide.structures.blocks.SourcesBlock;

public interface Edk2ElementBlockVisitor {
	void visit(DefinitionsBlock defs);
	void visit(SourcesBlock sources);
	void visit(PackagesBlock packages);
	void visit(LibraryClassesBlock libs);
}
