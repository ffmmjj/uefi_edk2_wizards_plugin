package org.uefiide.structures;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.blocks.Edk2ElementBlock;
import org.uefiide.utilities.ModuleInfParser;

public class Edk2Module extends Edk2Element {
	private ModuleInfParser parser;
	
	public Edk2Module(String path) {
		super(path);
		parser = new ModuleInfParser(this);
	}
	
	public Edk2Module(String path, String workspacePath) {
		super(path, workspacePath);
		parser = new ModuleInfParser(this);
	}

	public String getName() {
		return this.parser.getModuleName();
	}
	
	public Map<String, String> getDefines() {
		return this.parser.getModuleDefines();
	}
	
	public List<Edk2Package> getPackages() {
		List<Edk2Package> packages = new LinkedList<Edk2Package>();
		IPath workspacePath = new Path(this.getWorkspacePath());
		
		for(String packageName : this.parser.getModulePackages()) {
			packages.add(new Edk2Package(workspacePath.append(packageName).toString(), this.getWorkspacePath()));
		}
		
		return packages;
	}
	
	public List<String> getSources() {
		return this.parser.getModuleSources();
	}
	
	@Override
	public void save() {
		// TODO open a File to this module
		List<Edk2ElementBlock> blocks = this.parser.getRawBlocks();
		
		for(Edk2ElementBlock block : blocks) {
			// TODO write each block's toString() returned value into the module file
		}
	}
}