package org.uefiide.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.uefiide.projectmanip.NewEdk2ModuleProjectCreator;
import org.uefiide.structures.Edk2Module.Edk2ModuleType;
import org.uefiide.wizards.pages.ExistingModuleWizardPage;
import org.uefiide.wizards.pages.NewEdk2ModuleProjectPage;

public class NewEdk2ModuleProjectWizard extends Wizard implements INewWizard, IRunnableWithProgress {

	private NewEdk2ModuleProjectPage newModuleWizardPage;
	
	public NewEdk2ModuleProjectWizard() {
		super();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void addPages() {
		super.addPages();
		this.newModuleWizardPage = new NewEdk2ModuleProjectPage("New EDK2 Module Project");
		addPage(newModuleWizardPage);
	}
	
	@Override
	public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		arg0.beginTask("Creating New EDK2 module project", 10);

		NewEdk2ModuleProjectCreator.CreateNewEdk2ModuleProject(
				newModuleWizardPage.getNewModuleRootFolder(), 
				"TestModule.inf",
				newModuleWizardPage.getWorkspace(),
				arg0,
				Edk2ModuleType.UEFI_APPLICATION);

		arg0.done();
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

}
