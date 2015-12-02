package org.uefiide.projectmanip;

import org.uefiide.structures.Edk2Module.Edk2ModuleType;

public class ModuleProjectCreationContext {
	String moduleName;
	String moduleLocation;
	String workspaceLocation;
	Edk2ModuleType moduleType;
	
	String libraryClassName;
	String libraryClassHeaderLocation;
	
	boolean followsUefiDriverModel;
	
	public ModuleProjectCreationContext(String moduleName, String moduleLocation, String workspaceLocation, 
			Edk2ModuleType type, String libraryClassName, String libraryClassHeaderLocation,
			boolean followsUefiDriverModel) {
		this.moduleName = moduleName;
		this.moduleLocation = moduleLocation;
		this.workspaceLocation = workspaceLocation;
		this.moduleType = type;
		
		this.libraryClassName = libraryClassName;
		this.libraryClassHeaderLocation = libraryClassHeaderLocation;
		
		this.followsUefiDriverModel = followsUefiDriverModel;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public String getModuleLocation() {
		return this.moduleLocation;
	}
	
	public String getWorkspaceLocation() {
		return workspaceLocation;
	}
	
	public Edk2ModuleType getModuleType() {
		return moduleType;
	}
	
	public String getLibraryClassName() {
		return libraryClassName;
	}
	
	public String getLibraryClassHeaderLocation() {
		return this.libraryClassHeaderLocation;
	}
	
	public boolean followsUefiDriverModel() {
		return this.followsUefiDriverModel;
	}
}
