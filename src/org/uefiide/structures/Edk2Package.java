package org.uefiide.structures;


public class Edk2Package extends Edk2Element {
	private String path;
	
	public Edk2Package(String workspacePath, String path) {
		super(workspacePath);
		this.path = path;
	}

	public Iterable<String> getRelativeIncludePaths() {
		return null;
	}
	
	public String getPath() {
		return path;
	}
}
