package org.uefiide.projectmanip;

import org.eclipse.core.resources.IProject;

public class ProjectBuildConfigManager {
	private IProject project;
	
	public ProjectBuildConfigManager(IProject project) {
		this.project = project;
	}
	
	/**
	 * org.eclipse.cdt.managedbuilder.core.IBuilder shall be used to set the build command which invokes
	 * "build" from EDK2.
	 */
	
}
