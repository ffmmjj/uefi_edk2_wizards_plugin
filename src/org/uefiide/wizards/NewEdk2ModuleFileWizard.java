package org.uefiide.wizards;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.wizard.Wizard;
import org.uefiide.structures.Edk2Module;
import org.uefiide.wizards.pages.NewEdk2ModuleFileWizardpage;

public class NewEdk2ModuleFileWizard extends Wizard {
	NewEdk2ModuleFileWizardpage page;
	IProject project;

	public NewEdk2ModuleFileWizard(IProject project) {
		this.project = project;
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		super.addPages();
		this.page = new NewEdk2ModuleFileWizardpage(this.project);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		String newFileLocation = this.page.getNewFileLocation();
		String newFileName = this.page.getNewFileName();
		
		try {
			AddNewFileToProject(this.project.getWorkspace().getRoot().getFolder(new Path(newFileLocation)), newFileName);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void AddNewFileToProject(IFolder newFileLocation, String newFileName) throws IOException, CoreException {
		File file = new File(new Path(newFileLocation.getLocation().toString()).append(newFileName).toString());
		file.createNewFile();
		IFile newFile = newFileLocation.getFile(newFileName);
		newFile.createLink(new Path(file.getAbsolutePath()), IResource.VIRTUAL, null);
		
		Edk2Module module = getProjectModule();
		List<String> sources = module.getSources();
		sources.add(newFileName);
		module.setSources(sources);
		module.save();
	}
	
	private Edk2Module getProjectModule() {
		Edk2Module module = null;
		
		try {
			for(IResource res : this.project.members()) {
				if(res.getName().endsWith(".inf")) {
					module = new Edk2Module(res.getLocation().toString(), 
							this.project.getPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "EDK2_WORKSPACE")));
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return module;
	}

}
