package org.uefiide.structures.blocks;

import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;

public interface Edk2ElementBlock {
	void addLine(String line);
	void accept(Edk2ElementBlockVisitor visitor);
}
