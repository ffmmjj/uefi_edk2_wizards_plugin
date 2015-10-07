package org.uefiide.structures;

import java.util.List;

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
	
	public List<Edk2Package> getPackages() {
		return this.parser.getModulePackages();
	}
}