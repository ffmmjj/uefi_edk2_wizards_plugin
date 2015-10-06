package org.uefiide.structures;

public class Edk2Element {
	private String elementPath;
	private String workspacePath;
	
	public Edk2Element(String elementPath) {
		this(elementPath, inferWorkspaceFromElementPath(elementPath));
	}
	
	public Edk2Element(String elementPath, String workspacePath) throws IllegalArgumentException {
		if(workspacePath == null || workspacePath.isEmpty()) {
			throw new IllegalArgumentException("The EDK2 workspace path cannot be null or empty");
		}
		if(elementPath == null || elementPath.isEmpty()) {
			throw new IllegalArgumentException("The EDK2 element path cannot be null or empty");
		}
		
		this.workspacePath = workspacePath;
		this.elementPath = elementPath;
	}
	
	public String getWorkspacePath() {
		return this.workspacePath;
	}
	
	public String getElementPath() {
		return this.elementPath;
	}
	
	private static String inferWorkspaceFromElementPath(String elementPath) {
		return null;
	}
}
