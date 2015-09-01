package org.uefiide.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.uefiide.composites.ModulesSelectionComposite;
import org.uefiide.structures.Edk2Module;

public class WizardSelectModulesPage extends WizardPage {
	ModulesSelectionComposite pageComposite;

	public WizardSelectModulesPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		List<Edk2Module> modules = new ArrayList<>();
		modules.add(new Edk2Module("Hello", "AppPkg/Applications/Hello/Hello.inf"));
		modules.add(new Edk2Module("UefiBaseLib", "MdePkg/Library/UefiBaseLib/UefiBaseLib.inf"));
		modules.add(new Edk2Module("UsbDriver", "MdeModulePkg/Universal/Bus/UsbDriver/UsbDriver.inf"));
		
		pageComposite = new ModulesSelectionComposite(parent, SWT.NULL, modules);
		setControl(pageComposite);
	}

	public List<Edk2Module> getSelectedModules() {
		return pageComposite.getSelectedModules();
	}
}
