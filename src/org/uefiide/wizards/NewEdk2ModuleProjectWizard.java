package org.uefiide.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.uefiide.projectmanip.ExistingEdk2ModuleProjectCreator;
import org.uefiide.projectmanip.NewEdk2ModuleProjectCreator;
import org.uefiide.structures.Edk2Module;
import org.uefiide.wizards.pages.ExistingModuleWizardPage;

public class NewEdk2ModuleProjectWizard extends Wizard implements INewWizard, IRunnableWithProgress {

	private ExistingModuleWizardPage existingModuleWizardPage;
	
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
		this.existingModuleWizardPage = new ExistingModuleWizardPage("New EDK2 Module Project");
		addPage(existingModuleWizardPage);
	}
	
	@Override
	public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		arg0.beginTask("Creating New EDK2 module project", 10);

		NewEdk2ModuleProjectCreator.CreateNewEdk2ModuleProject(
				"C:\\Users\\ffmmj\\Documents\\dev\\UDK2014_workspace\\AppPkg\\Applications\\TestModule", 
				"TestModule.inf",
				"C:\\Users\\ffmmj\\Documents\\dev\\UDK2014_workspace",
				arg0);

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
