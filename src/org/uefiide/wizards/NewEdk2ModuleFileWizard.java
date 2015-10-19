package org.uefiide.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.uefiide.wizards.pages.NewEdk2ModuleFileWizardpage;

public class NewEdk2ModuleFileWizard extends Wizard {
	NewEdk2ModuleFileWizardpage page;

	public NewEdk2ModuleFileWizard() {
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
		return false;
	}

}
