package org.uefiide.structures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public abstract class Edk2Element {
	private String elementPath;
	private String workspacePath;
	InputStream contents;
	
	public Edk2Element(String elementPath) {
		this(elementPath, inferWorkspaceFromElementPath(elementPath));
	}
	
	public Edk2Element(String elementPath, String workspacePath, InputStream contents) {
		this(elementPath, workspacePath, false);
		this.contents = contents;
	}
	
	public Edk2Element(String elementPath, String workspacePath) throws IllegalArgumentException {
		this(elementPath, workspacePath, true);
		try {
			this.contents = new FileInputStream(elementPath);
		} catch (FileNotFoundException e) {
			// This is not supposed to happen!
			e.printStackTrace();
		}
	}
	
	private Edk2Element(String elementPath, String workspacePath, boolean checkFileExists) throws IllegalArgumentException {
		if(workspacePath == null || workspacePath.isEmpty()) {
			throw new IllegalArgumentException("The EDK2 workspace path cannot be null or empty");
		}
		if(elementPath == null || elementPath.isEmpty()) {
			throw new IllegalArgumentException("The EDK2 element path cannot be null or empty");
		}
		if(checkFileExists && !(new File(elementPath).exists())) {
			throw new IllegalArgumentException("The path \"" + elementPath + "\" of the EDK2 element could not be found");
		}
		IPath baseToolsPath = new Path(workspacePath).append("BaseTools");
		if(!(new File(baseToolsPath.toString()).exists())) {
			throw new IllegalArgumentException("The path \"" + workspacePath + "\" does not contain the BaseTools folder");
		}
		
		this.elementPath = elementPath;
		this.workspacePath = workspacePath;
	}
	
	public String getWorkspacePath() {
		return this.workspacePath;
	}
	
	public String getElementPath() {
		return this.elementPath;
	}
	
	public InputStream getContentsStream() {
		return this.contents;
	}
	
	public static String inferWorkspaceFromElementPath(String elementPath) {
		IPath currentDir = new Path(elementPath);
		while(!currentDir.isEmpty()) {
			IPath tentativeBaseTools = currentDir.append("BaseTools");
			File baseToolsDir = new File(tentativeBaseTools.toString());
			if(baseToolsDir.exists()) {
				return currentDir.toString();
			}
			
			if(currentDir.isRoot()) {
				break;
			}
			currentDir = currentDir.removeLastSegments(1);
		}
		
		throw new IllegalArgumentException("The workspace cannot be inferred from this element path");
	}
	
	public abstract void save();
}
