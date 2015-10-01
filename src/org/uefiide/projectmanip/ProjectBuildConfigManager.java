package org.uefiide.projectmanip;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.uefiide.structures.Edk2Module;

public class ProjectBuildConfigManager {
	
	public static void setEDK2BuildCommands(IProject project, Edk2Module edk2Module) {
		ICProjectDescription projDesc = CCorePlugin.getDefault().getProjectDescription(project, true);
		IConfiguration cfg = ManagedBuildManager.getConfigurationForDescription(projDesc.getActiveConfiguration());
		IBuilder builder = cfg.getBuilder();
		try {
			builder.setBuildArguments("-p AppPkg/AppPkg.dsc");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * org.eclipse.cdt.managedbuilder.core.IBuilder shall be used to set the build command which invokes
	 * "build" from EDK2.
	 */
	
}
