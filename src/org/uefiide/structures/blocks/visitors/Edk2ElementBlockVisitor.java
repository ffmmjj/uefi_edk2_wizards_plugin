package org.uefiide.structures.blocks.visitors;

import org.uefiide.structures.blocks.DefinitionsBlock;
import org.uefiide.structures.blocks.PackagesBlock;
import org.uefiide.structures.blocks.SourcesBlock;

public interface Edk2ElementBlockVisitor {
	void visit(DefinitionsBlock defs);
	void visit(SourcesBlock defs);
	void visit(PackagesBlock defs);
}
