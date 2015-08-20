package org.uefiide.structures;

public class Edk2Element {
	private String workspacePath;
	
	public Edk2Element(String workspacePath) throws IllegalArgumentException {
		if(workspacePath == null || workspacePath.isEmpty()) {
			throw new IllegalArgumentException("The EDK2 workspace path cannot be null or empty");
		}
		this.workspacePath = workspacePath;
	}
	
	public String getWorkspacePath() {
		return this.workspacePath;
	}
}
