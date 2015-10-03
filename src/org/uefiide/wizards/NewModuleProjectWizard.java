package org.uefiide.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
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
import org.uefiide.wizards.pages.NewModuleWizardPage;

public class NewModuleProjectWizard extends Wizard implements INewWizard, IRunnableWithProgress {

	private NewModuleWizardPage newModuleWizardPage;

	public NewModuleProjectWizard() {
		super();
	}

	public void addPages() {
		super.addPages();
		this.newModuleWizardPage = new NewModuleWizardPage("New EDK2 Module Project");
		addPage(newModuleWizardPage);
	}

	@Override
	public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		IWorkspaceRoot wrkSpaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IPath newProjectPath = new Path(wrkSpaceRoot.getLocation().append(newModuleWizardPage.getProjName()).toString());
		IProject newProjectHandle = wrkSpaceRoot.getProject(newModuleWizardPage.getProjName());
		
		arg0.beginTask("creating EDK2 module project", 10);
				 
		IProjectDescription projDesc;
		try {
			newProjectHandle.create(arg0);
			newProjectHandle.open(arg0);
			//projDesc = ResourcesPlugin.getWorkspace().newProjectDescription(newProjectHandle.getName());
			projDesc = ResourcesPlugin.getWorkspace().newProjectDescription(newModuleWizardPage.getProjName());
			projDesc.setLocation(newProjectPath);
			
			/*ProjectCreatedActions prjCreator = new ProjectCreatedActions();
			prjCreator.setProject(newProjectHandle);
			prjCreator.setProjectLocation(newProjectPath);
			prjCreator.setConfigs(new Configuration[0]);
			prjCreator.createProject(null, CCorePlugin.DEFAULT_INDEXER, true);*/
			CCorePlugin.getDefault().createCDTProject(projDesc, newProjectHandle, null);

			// Set up build information
			ICProjectDescriptionManager pdMgr = CoreModel.getDefault().getProjectDescriptionManager();
			ICProjectDescription cProjDesc = pdMgr.createProjectDescription(newProjectHandle, false);
			ManagedBuildInfo info = ManagedBuildManager.createBuildInfo(newProjectHandle);
			ManagedProject mProj = new ManagedProject(cProjDesc);
			info.setManagedProject(mProj);

			CfgHolder cfgHolder = new CfgHolder(null, null);
			String s = "0";
			Configuration config = new Configuration(mProj, (ToolChain)null, ManagedBuildManager.calculateChildId(s, null), cfgHolder.getName());
			IBuilder builder = config.getEditableBuilder();
			builder.setManagedBuildOn(false);
			CConfigurationData data = config.getConfigurationData();
			cProjDesc.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, data);

			pdMgr.setProjectDescription(newProjectHandle, cProjDesc);
			
			Edk2ModuleProjectCreator.CreateProjectStructure(newProjectHandle, new Path(newModuleWizardPage.getLocation()).removeLastSegments(1).toString());
			//new ProjectSettingsManager(newProjectHandle).setIncludePaths(new ArrayList<String>());
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
