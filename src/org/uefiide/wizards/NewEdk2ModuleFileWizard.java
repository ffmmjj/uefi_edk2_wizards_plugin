package org.uefiide.wizards;

import java.io.File;
import java.io.IOException;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.Wizard;
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
		this.page = new NewEdk2ModuleFileWizardpage();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		String newFileLocation = this.page.getNewFileLocation();
		String newFileName = this.page.getNewFileName();
		
		try {
			AddNewFileToProject(newFileLocation, newFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void AddNewFileToProject(String newFileLocation, String newFileName) throws IOException {
		File file = new File(new Path(newFileLocation).append(newFileName).toString());
		file.createNewFile();
	}

}
