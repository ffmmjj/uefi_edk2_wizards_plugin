package org.uefiide.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.uefiide.projectmanip.Edk2ModuleProjectCreator;
import org.uefiide.projectmanip.ProjectBuildConfigManager;
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
			
			List<Edk2Package> modulePackages;
			Edk2Module projectModule = null;
			if(existingModuleWizardPage.shouldInferWorkspacePath()) {
				projectModule = new Edk2Module(existingModuleWizardPage.getLocation());
			} else {
				projectModule = new Edk2Module(existingModuleWizardPage.getLocation(), existingModuleWizardPage.getWorkspacePath());
			}
			modulePackages = projectModule.getPackages();
			
			List<String> includePaths = new LinkedList<String>();
			for(Edk2Package p : modulePackages) {
				includePaths.addAll(p.getAbsoluteIncludePaths());
			}
			ProjectSettingsManager.setIncludePaths(newProjectHandle, includePaths);
			
			ProjectBuildConfigManager.setEDK2BuildCommands(newProjectHandle, null);
			
			newProjectHandle.setPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "EDK2_WORKSPACE"), projectModule.getWorkspacePath());
			newProjectHandle.setPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "MODULE_ROOT_PATH"), new Path(projectModule.getElementPath()).removeLastSegments(1).toString());
		} catch (CoreException e1) {
			e1.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
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
