package org.uefiide.structures;

import java.util.Iterator;

import org.uefiide.utilities.ModuleInfParser;

public class Edk2Module {
	private String path; // full path to .inf
	private String workspacePath;
	private ModuleInfParser parser;
	
	public Edk2Module(String path, String workspacePath) {
		this.path = path;
		this.workspacePath = workspacePath;
		parser = new ModuleInfParser(this);
	}

	public String getPath() {
		return path;
	}
	
	public String getWorkspacePath() {
		return this.workspacePath;
	}
	
	public String getName() {
		return this.parser.getModuleName();
	}
	
	public Iterator<Edk2Package> getPackages() {
		return this.parser.getModulePackages();
	}
}