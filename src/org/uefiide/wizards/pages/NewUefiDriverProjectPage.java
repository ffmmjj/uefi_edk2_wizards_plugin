package org.uefiide.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;

public class NewUefiDriverProjectPage extends WizardPage {
	Button btnDriverFollowsUefi;
	
	/**
	 * Create the wizard.
	 */
	public NewUefiDriverProjectPage() {
		super("UefiDriverPage");
		setTitle("New UEFI Driver Project");
		setDescription("Enter information about the new UEFI Driver.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));

		setControl(container);
		
		btnDriverFollowsUefi = new Button(container, SWT.CHECK);
		btnDriverFollowsUefi.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnDriverFollowsUefi.setText("Driver follows UEFI Driver Model?");
	}
	
	public boolean FollowsUefiDriverModel() {
		return btnDriverFollowsUefi.getSelection();
	}
}
