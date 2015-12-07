package org.uefiide.wizards.pages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewLibraryClassProjectPage extends WizardPage implements ModifyListener, MouseListener {
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
		
		addListeners();
		
		setControl(container);
	}
	
	private void addListeners() {
		libraryClassName.addModifyListener(this);
		libraryClassHeaderPath.addModifyListener(this);
		libraryClassHeaderPathBtn.addMouseListener(this);
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

	@Override
	public void modifyText(ModifyEvent e) {
		this.setPageComplete(!(libraryClassName.getText().isEmpty() || libraryClassHeaderPath.getText().isEmpty()));
	}
	
	@Override
	public IWizardPage getNextPage() {
		return null;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		FileDialog fileDialog = new FileDialog(libraryClassHeaderPathBtn.getShell());
		fileDialog.setText("Select the header file corresponding to this library class");
		String path = fileDialog.open();
		if(path != null) {
			libraryClassHeaderPath.setText(path);
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
