package org.uefiide.events.internals;

import org.eclipse.core.resources.IProject;
import org.uefiide.structures.Edk2Module;

public class Edk2ModuleChangeEvent {
	private IProject project;
	private Edk2Module oldModule;
	private Edk2Module newModule;
	
	public Edk2ModuleChangeEvent(IProject project, Edk2Module oldModule, Edk2Module newModule) {
		this.project = project;
		this.newModule = newModule;
		this.oldModule = oldModule;
	}

	public IProject getProject() {
		return project;
	}

	public Edk2Module getOldModule() {
		return oldModule;
	}
	
	public Edk2Module getNewModule() {
		return newModule;
	}
}