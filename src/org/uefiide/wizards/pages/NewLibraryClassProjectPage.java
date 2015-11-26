package org.uefiide.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class NewLibraryClassProjectPage extends WizardPage {
	Text libraryClassName;
	Text libraryClassHeaderPath;
	Button libraryClassHeaderPathBtn;
	
	public NewLibraryClassProjectPage() {
		super("NewLibraryClassPage");
		setTitle("New Library Class");
		setDescription("Enter information specific for this new EDK2 library class project.");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		
		createLibraryClassNameFormInput(container);
		createLibraryClassHeaderPathFormInput(container);
		
		setControl(container);
	}
	
	private void createLibraryClassHeaderPathFormInput(Composite container) {
		Label moduleRootLabel = new Label(container, SWT.NULL);
		moduleRootLabel.setText("Enter the library class header location:");
		
		SashForm form = new SashForm(container, SWT.HORIZONTAL | SWT.NULL);
		form.setLayout(new GridLayout(1, false));
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		form.setSashWidth(0);
		
		Composite libraryClassHeaderPathTxtContainer = new Composite(form, SWT.NULL);
		libraryClassHeaderPathTxtContainer.setLayout(new GridLayout(1, false));
		libraryClassHeaderPathTxtContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		libraryClassHeaderPath = new Text(libraryClassHeaderPathTxtContainer, SWT.BOLD | SWT.BORDER);
		libraryClassHeaderPath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite libraryClassHeaderPathBtnContainer = new Composite(form, SWT.NULL);
		libraryClassHeaderPathBtnContainer.setLayout(new GridLayout(1, false));
		libraryClassHeaderPathBtnContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		libraryClassHeaderPathBtn = new Button(libraryClassHeaderPathBtnContainer, SWT.PUSH);
		libraryClassHeaderPathBtn.setText("Browse");
		libraryClassHeaderPathBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		form.setWeights(new int[]{7, 1});
	}

	private void createLibraryClassNameFormInput(Composite container) {
		Label libraryClassNameLabel = new Label(container, SWT.NULL);
		libraryClassNameLabel.setText("Enter the library class' name:");
		
		libraryClassName = new Text(container, SWT.BOLD | SWT.BORDER);
		libraryClassName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
	}

	public String GetLibraryClassName() {
		return libraryClassName.getText();
	}
	
	public String GetLibraryClassHeaderPath() {
		return libraryClassHeaderPath.getText();
	}
}
