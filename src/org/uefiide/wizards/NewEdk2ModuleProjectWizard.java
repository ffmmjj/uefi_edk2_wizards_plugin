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
import org.uefiide.projectmanip.Edk2ModuleProjectCreator;
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
		try {
			arg0.beginTask("Creating New EDK2 module project", 10);
			IPath moduleRoot = new Path("C:\\Users\\ffmmj\\Documents\\dev\\UDK2014_workspace\\AppPkg\\Applications\\TestModule");
			IPath infPath = moduleRoot.append("TestModule.inf");
			
			arg0.beginTask("Creating new module root folder", 20);
			File infRootFolder = new File(moduleRoot.toString());
			if(!infRootFolder.exists()) {
				infRootFolder.mkdirs();
			}
			
			arg0.beginTask("Creating new module inf file", 30);
			File infFile = new File(infPath.toString());
			infFile.createNewFile();
			
			FileOutputStream fop = new FileOutputStream(infFile);
			fop.write("[Defines]\n".getBytes());
			fop.write("  MODULE_TYPE = UEFI_APPLICATION\n".getBytes());
			fop.write("  INF_VERSION = 0x00010006\n".getBytes());
			fop.write("  BASE_NAME = TestModule\n".getBytes());
			fop.write("  VERSION_STRING = 0.1\n".getBytes());
			fop.write("  FILE_GUID = a912f198-7f0e-4803-b918-b757b80cec83\n".getBytes());
			fop.write("  ENTRY_POINT = ShellCEntryLib\n".getBytes());
			fop.write("[Sources]\n".getBytes());
			fop.write("  TestModule.c\n".getBytes());
			fop.write("[Packages]\n".getBytes());
			fop.write("  MdePkg/MdePkg.dec\n".getBytes());
			fop.write("  ShellPkg/ShellPkg.dec\n".getBytes());
			fop.write("[LibraryClasses]\n".getBytes());
			fop.write("  UefiLib\n".getBytes());
			fop.write("  ShellCEntryLib\n".getBytes());
			fop.flush();
			fop.close();
			
			arg0.beginTask("Creating project from new module", 40);
			Edk2Module projectModule = new Edk2Module(infPath.toString());
			Edk2ModuleProjectCreator.CreateEDK2ProjectFromExistingModule(projectModule, arg0);
			arg0.done();
		} catch (CoreException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

}
