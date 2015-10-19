package org.uefiide.wizards;

import org.eclipse.jface.wizard.Wizard;

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
		return false;
	}

}
