package org.uefiide.structures;

import java.util.List;

import org.uefiide.structures.parsers.PackageDecParser;


public class Edk2Package extends Edk2Element {
	private PackageDecParser parser;
	
	public Edk2Package(String path) {
		super(path);
		this.parser = new PackageDecParser(this);
	}
	
	public Edk2Package(String path, String workspacePath) {
		super(path, workspacePath);
		this.parser = new PackageDecParser(this);
	}

	public List<String> getAbsoluteIncludePaths() {
		return this.parser.getIncludePaths(); 
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
}
