package org.uefiide.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.internal.core.pdom.indexer.IndexerPreferences;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.cdt.managedbuilder.internal.core.ToolChain;
import org.eclipse.cdt.managedbuilder.templateengine.ProjectCreatedActions;
import org.eclipse.cdt.managedbuilder.ui.wizards.CfgHolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.uefiide.projectmanip.Edk2ModuleProjectCreator;
import org.uefiide.projectmanip.ProjectBuildConfigManager;
import org.uefiide.projectmanip.ProjectCreator;
import org.uefiide.projectmanip.ProjectSettingsManager;
import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.Edk2Package;
import org.uefiide.wizards.pages.ExistingModuleWizardPage;

public class ExistingModuleProjectWizard extends Wizard implements INewWizard, IRunnableWithProgress {

	private ExistingModuleWizardPage existingModuleWizardPage;

	public ExistingModuleProjectWizard() {
		super();
	}

	public void addPages() {
		super.addPages();
		this.existingModuleWizardPage = new ExistingModuleWizardPage("Existing EDK2 Module Project");
		addPage(existingModuleWizardPage);
	}

	@Override
	public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		IWorkspaceRoot wrkSpaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IPath newProjectPath = new Path(wrkSpaceRoot.getLocation().append(existingModuleWizardPage.getProjName()).toString());
		IProject newProjectHandle = wrkSpaceRoot.getProject(existingModuleWizardPage.getProjName());
		
		arg0.beginTask("Creating Existing EDK2 module project", 10);
				 
		IProjectDescription projDesc;
		try {
			newProjectHandle.create(arg0);
			newProjectHandle.open(arg0);
			projDesc = ResourcesPlugin.getWorkspace().newProjectDescription(existingModuleWizardPage.getProjName());
			projDesc.setLocation(newProjectPath);
			
			CCorePlugin.getDefault().createCDTProject(projDesc, newProjectHandle, null);
			
			Edk2ModuleProjectCreator.ConfigureProjectNature(newProjectHandle);
			Edk2ModuleProjectCreator.CreateProjectStructure(newProjectHandle, new Path(existingModuleWizardPage.getLocation()).removeLastSegments(1).toString());
			List<Edk2Package> modulePackages = new Edk2Module(existingModuleWizardPage.getLocation()).getPackages();
			
			List<String> includePaths = new LinkedList<String>();
			for(Edk2Package p : modulePackages) {
				includePaths.addAll(p.getAbsoluteIncludePaths());
			}
			//Edk2Package edk2Package = new Edk2Package("/home/felipe/dev/repos/git/edk2/MdePkg/MdePkg.dec");
			//new ProjectSettingsManager(newProjectHandle).setIncludePaths(edk2Package.getAbsoluteIncludePaths());
			ProjectSettingsManager.setIncludePaths(newProjectHandle, includePaths);
			
			ProjectBuildConfigManager.setEDK2BuildCommands(newProjectHandle, null);
			
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public boolean performFinish() {
		try {
			getContainer().run(false, true, this);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}
}
