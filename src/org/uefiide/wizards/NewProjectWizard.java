package org.uefiide.wizards;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.core.settings.model.extension.CFolderData;
import org.eclipse.cdt.core.settings.model.extension.CLanguageData;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IProjectActionFilter;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class NewProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage edk2RootSelectionPage;
	private WizardSelectModulesPage wizardSelectModulesPage;
	
	public NewProjectWizard() {
		setWindowTitle("New EDK2 Project");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		System.out.println(edk2RootSelectionPage.getLocationPath().toString());
		System.out.println(edk2RootSelectionPage.getProjectName());
		
		IProject cdtProject = ResourcesPlugin.getWorkspace().getRoot().getProject("MyCProject");
		ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(cdtProject);
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
        		System.out.println(pathEntry.getName());
        		System.out.println(pathEntry.getValue());
        	}
        }
		
		return false;
	}

	@Override
	 public void addPages() {
		 super.addPages();
		
		 edk2RootSelectionPage = new WizardNewProjectCreationPage("EDK2 root selection page");
		 edk2RootSelectionPage.setTitle("EDK2 root selection");
		 edk2RootSelectionPage.setDescription("Select the path to the EDK2 root");
		 addPage(edk2RootSelectionPage);
		
		 wizardSelectModulesPage = new WizardSelectModulesPage("EDK Modules selection page");
		 wizardSelectModulesPage.setTitle("EDK Modules selection page");
		 wizardSelectModulesPage.setDescription("Select the EDK2 modules that will be used in the new project");
		 addPage(wizardSelectModulesPage);
	 }
}
