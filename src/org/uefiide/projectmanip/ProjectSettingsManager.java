package org.uefiide.projectmanip;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.core.settings.model.extension.CFolderData;
import org.eclipse.cdt.core.settings.model.extension.CLanguageData;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class ProjectSettingsManager {
	private IProject project;
	
	public ProjectSettingsManager(IProject project) {
		this.project = project;		
	}
	
	public void setIncludePaths(List<String> includePaths) {
		ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(this.project);
		ICLanguageSettingEntry[] entries = ĩncludePathString2ICLanguageSettingEntryArray(includePaths);
		
		// Set include paths for all configurations in the CDT project
		for(ICConfigurationDescription confDesc : projectDescription.getConfigurations()) {
			IConfiguration configuration = ManagedBuildManager.getConfigurationForDescription(confDesc);
			CConfigurationData Cconfigdata = configuration.getConfigurationData();
	        CFolderData rootFolderData = Cconfigdata.getRootFolderData();
	        //configuration.setBuildCommand("build2");   // This sets the build command!
	        //configuration.setBuildArguments("-t GCC47 -a X64 -p AppPkg/AppPkg.dsc -m AppPkg/Applications/Hello/Hello.inf"); // This sets the build command arguments!
	        CLanguageData[] languageDatas = rootFolderData.getLanguageDatas();
	        
	        for(CLanguageData languageData : languageDatas) {
	        	languageData.setEntries(ICLanguageSettingEntry.INCLUDE_PATH, entries);
	        }
		}
		
		try {
			CoreModel.getDefault().setProjectDescription(project, projectDescription);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> getIncludePaths() {
		List<String> includePaths = new ArrayList<>();
		
		ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(this.project);
		IConfiguration configuration =
	            ManagedBuildManager.getConfigurationForDescription(projectDescription
	                                        .getActiveConfiguration());
		ICLanguageSettingEntry[] includePathEntries = null ;
		CConfigurationData Cconfigdata = configuration.getConfigurationData();
        CFolderData rootFolderData = Cconfigdata.getRootFolderData();
        CLanguageData[] languageDatas = rootFolderData.getLanguageDatas();
        
        for(CLanguageData languageData : languageDatas) {
        	includePathEntries =languageData.getEntries(ICLanguageSettingEntry.INCLUDE_PATH);
        	for(ICLanguageSettingEntry pathEntry : includePathEntries) {
        		includePaths.add(pathEntry.getValue());
        	}
        }
        
		return includePaths;
	}
	
	private ICLanguageSettingEntry[] ĩncludePathString2ICLanguageSettingEntryArray(List<String> paths) {
		List<ICLanguageSettingEntry> entries = new ArrayList<>();
		ICLanguageSettingEntry[] entriesArray = new ICLanguageSettingEntry[paths.size()];
		
		for(String path : paths) {
			entries.add(new CIncludePathEntry(path, ICSettingEntry.LOCAL));
		}
		entries.toArray(entriesArray);
		
		return entriesArray;
	}
}
