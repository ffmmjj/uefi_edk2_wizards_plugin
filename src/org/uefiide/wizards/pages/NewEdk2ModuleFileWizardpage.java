package org.uefiide.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewEdk2ModuleFileWizardpage extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public NewEdk2ModuleFileWizardpage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setBounds(15, 25, 300, 400);

		Label dirLocationLabel = new Label(container,SWT.NONE);
		dirLocationLabel.setText("Enter the file location:");
		dirLocationLabel.setBounds(50, 95, 150, 27);

		Text dirLocation = new Text(container,SWT.BOLD | SWT.BORDER);
		dirLocation.setBounds(220, 90, 170, 27);

		Button dirLocationBtn = new Button(container,SWT.NONE);
		dirLocationBtn.setText("Browse");
		dirLocationBtn.setBounds(395, 90, 80, 27);
		
		Label sourceNameLabel = new Label(container,SWT.NONE);
		sourceNameLabel.setText("Enter the new file name:");
		sourceNameLabel.setBounds(30, 160 ,185, 27);
		
		Text sourceName = new Text(container,SWT.BOLD | SWT.BORDER);
		sourceName.setBounds(220, 150, 170, 27);

		setControl(container);
	}

}
