package org.uefiide.structures;

import java.util.List;

import org.uefiide.utilities.PackageDecParser;


public class Edk2Package extends Edk2Element {
	private String path;
	private PackageDecParser parser;
	
	public Edk2Package(String workspacePath, String path) {
		super(workspacePath);
		this.path = path;
		this.parser = new PackageDecParser(this);
	}

	public List<String> getAbsoluteIncludePaths() {
		return this.parser.getIncludePaths(); 
	}
	
	public String getPath() {
		return path;
	}
	
}
