package org.uefiide.projectmanip;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.core.settings.model.extension.CFolderData;
import org.eclipse.cdt.core.settings.model.extension.CLanguageData;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.uefiide.structures.Edk2Module;

public class ProjectBuildConfigManager {
	
	public static void setEDK2BuildCommands(IProject project, Edk2Module edk2Module) {
		/*
		ICProjectDescription projDesc = CCorePlugin.getDefault().getProjectDescription(project, true);
		IConfiguration cfg = ManagedBuildManager.getConfigurationForDescription(projDesc.getActiveConfiguration());
		IBuilder builder = cfg.getBuilder();
		try {
			builder.setBuildArguments("-p AppPkg/AppPkg.dsc");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(project);
		
		// Set include paths for all configurations in the CDT project
		for(ICConfigurationDescription confDesc : projectDescription.getConfigurations()) {
			IConfiguration configuration = ManagedBuildManager.getConfigurationForDescription(confDesc);
			CConfigurationData Cconfigdata = configuration.getConfigurationData();
	        CFolderData rootFolderData = Cconfigdata.getRootFolderData();
	        configuration.setBuildCommand("build");   // This sets the build command!
	        configuration.setBuildArguments("-t GCC47 -a X64 -p AppPkg/AppPkg.dsc -m AppPkg/Applications/Hello/Hello.inf"); // This sets the build command arguments!
	        CLanguageData[] languageDatas = rootFolderData.getLanguageDatas();
	        
	        /*
	        for(CLanguageData languageData : languageDatas) {
	        	languageData.setEntries(ICLanguageSettingEntry.INCLUDE_PATH, entries);
	        }*/
		}
		
		try {
			CoreModel.getDefault().setProjectDescription(project, projectDescription);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/**
	 * org.eclipse.cdt.managedbuilder.core.IBuilder shall be used to set the build command which invokes
	 * "build" from EDK2.
	 */
	}
}
