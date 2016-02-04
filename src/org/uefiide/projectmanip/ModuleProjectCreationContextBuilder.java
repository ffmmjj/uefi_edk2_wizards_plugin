package org.uefiide.projectmanip;

import org.uefiide.structures.Edk2Module.Edk2ModuleType;

public class ModuleProjectCreationContextBuilder {
	String moduleName;
	String moduleLocation;
	String workspaceLocation;
	Edk2ModuleType moduleType;
	
	String libraryClassName;
	String libraryClassHeaderLocation;
	
	boolean followsUefiDriverModel;
	
	public ModuleProjectCreationContextBuilder() {
		
	}
	
	public ModuleProjectCreationContextBuilder withModuleName(String moduleName) {
		this.moduleName = moduleName;
		return this;
	}
	
	public ModuleProjectCreationContextBuilder withModuleLocation(String moduleLocation) {
		this.moduleLocation = moduleLocation;
		return this;
	}
	
	public ModuleProjectCreationContextBuilder withWorkspaceLocation(String workspaceLocation) {
		this.workspaceLocation = workspaceLocation;
		return this;
	}
	
	public ModuleProjectCreationContextBuilder withModuleType(Edk2ModuleType type)  {
		this.moduleType = type;
		return this;
	}
	
	public ModuleProjectCreationContextBuilder withLibraryClassName(String libraryClassName) {
		this.libraryClassName = libraryClassName;
		return this;
	}
	
	public ModuleProjectCreationContextBuilder withLibraryClassHeaderLocation(String libraryClassHeaderLocation) {
		this.libraryClassHeaderLocation = libraryClassHeaderLocation;
		return this;
	}
	
	public ModuleProjectCreationContextBuilder withFollowsUefiDriverModel(boolean followsUefiDriverModel) {
		this.followsUefiDriverModel = followsUefiDriverModel;
		return this;
	}
	
	public ModuleProjectCreationContext build() {
		return new ModuleProjectCreationContext(moduleName, moduleLocation, workspaceLocation, 
				moduleType, libraryClassName, libraryClassHeaderLocation, followsUefiDriverModel);
	}
}
