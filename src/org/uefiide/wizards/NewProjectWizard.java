package org.uefiide.wizards;

import java.net.URI;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.ui.wizards.NewMakeProjFromExisting;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.uefiide.projectmanip.ProjectCreator;

/**
 * 
 * @author felipe
 * Inspired by the tutorials found on 
 * https://cvalcarcel.wordpress.com/2009/07/26/writing-an-eclipse-plug-in-part-4-create-a-custom-project-in-eclipse-new-project-wizard-the-behavior/
 */
public class NewProjectWizard extends NewMakeProjFromExisting implements INewWizard {

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
		super.performFinish();
		String name = edk2RootSelectionPage.getProjectName();
	    URI location = null;
	    if (!edk2RootSelectionPage.useDefaults()) {
	        location = edk2RootSelectionPage.getLocationURI();
	    } // else location == null
	 
	    try {
			ProjectCreator.createEdk2Project(name, location, wizardSelectModulesPage.getSelectedModules());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return true;
		/*
		System.out.println(edk2RootSelectionPage.getLocationPath().toString());
		System.out.println(edk2RootSelectionPage.getProjectName());
		
		IProject cdtProject = ResourcesPlugin.getWorkspace().getRoot().getProject("MyCProject");
				
		ProjectSettingsManager manager = new ProjectSettingsManager(cdtProject);
		List<String> includePaths = new ArrayList<>();
		
		includePaths.add("/home/felipe/Desktop/dummy_include_path");
		manager.setIncludePaths(includePaths);
		
		for(String p : manager.getIncludePaths()) {
			System.out.println(p);
		}
		return false;
		*/
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
