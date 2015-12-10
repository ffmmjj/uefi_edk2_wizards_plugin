package org.uefiide.wizards.pages;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.uefiide.structures.Edk2Module;

public class ExistingModuleWizardPage extends WizardPage implements ModifyListener {
	private Button infBrowserBtn;
	private Text infLocationPath;
	private Button detectWorkspaceFromModuleBtn;
	private Button workspacePathBtn;
	private Text workspacePath;
	Label workspacePathLabel;

	public ExistingModuleWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Opens an EDK2 module project");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));

		createModuleLocationFormInput(container);
		createWorkspaceFormInput(container);
		addListeners();

		setControl(container);
	}
	
	private void createModuleLocationFormInput(Composite container) {
		Label infPathLabel = new Label(container, SWT.NULL);
		infPathLabel.setText("Enter the module's .inf location:");
		
		SashForm form = new SashForm(container, SWT.HORIZONTAL | SWT.NULL);
		form.setLayout(new GridLayout(1, false));
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		form.setSashWidth(0);
		
		Composite infLocationPathContainer = new Composite(form, SWT.NULL);
		infLocationPathContainer.setLayout(new GridLayout(1, false));
		infLocationPathContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		infLocationPath = new Text(infLocationPathContainer, SWT.BOLD | SWT.BORDER);
		infLocationPath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite infBrowserBtnContainer = new Composite(form, SWT.NULL);
		infBrowserBtnContainer.setLayout(new GridLayout(1, false));
		infBrowserBtnContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		infBrowserBtn = new Button(infBrowserBtnContainer, SWT.PUSH);
		infBrowserBtn.setText("Browse");
		infBrowserBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		form.setWeights(new int[]{7, 1});
	}
	
	private void createWorkspaceFormInput(Composite container) {
		detectWorkspaceFromModuleBtn = new Button(container, SWT.CHECK);
		detectWorkspaceFromModuleBtn.setText("Detect workspace from module path");
		detectWorkspaceFromModuleBtn.setSelection(true);
		
		workspacePathLabel = new Label(container, SWT.NULL);
		workspacePathLabel.setText("Enter the workspace path:");
		workspacePathLabel.setEnabled(false);
		
		SashForm form = new SashForm(container, SWT.HORIZONTAL | SWT.NULL);
		form.setLayout(new GridLayout(1, false));
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite workspacePathComposite = new Composite(form, SWT.NULL);
		workspacePathComposite.setLayout(new GridLayout(1, true));
		workspacePathComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		workspacePath = new Text(workspacePathComposite, SWT.BOLD | SWT.BORDER);
		workspacePath.setEnabled(false);
		workspacePath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite workspacePathBtnComposite = new Composite(form, SWT.NULL);
		workspacePathBtnComposite.setLayout(new GridLayout());
		workspacePathBtnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		workspacePathBtn = new Button(workspacePathBtnComposite, SWT.PUSH);
		workspacePathBtn.setText("Browse");
		workspacePathBtn.setEnabled(false);
		workspacePathBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		form.setWeights(new int[]{7, 1});
	}

	private void addListeners() {
		infBrowserBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog fileDialog = new FileDialog(infBrowserBtn.getShell());
				fileDialog.setText("Select the parent directory for project");
				String path = fileDialog.open();
				if(path != null) {
					infLocationPath.setText(path);
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});
		workspacePathBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
				DirectoryDialog dirDialog = new DirectoryDialog(workspacePathBtn.getShell());
				dirDialog.setText("Select the EDK2 workspace directory");
				String path = dirDialog.open();
				if(path != null) {
					workspacePath.setText(path);
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});
		detectWorkspaceFromModuleBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ExistingModuleWizardPage.this.workspacePathLabel.setEnabled(!shouldInferWorkspacePath());
				ExistingModuleWizardPage.this.workspacePath.setEnabled(!shouldInferWorkspacePath());
				ExistingModuleWizardPage.this.workspacePathBtn.setEnabled(!shouldInferWorkspacePath());
			}
		});
		
		infLocationPath.addModifyListener(this);
	}

	public String getProjName(){
		return new Path(getLocation()).removeFileExtension().lastSegment().toString();
	}

	public String getLocation(){
		return infLocationPath.getText();
	}
	
	public boolean shouldInferWorkspacePath() {
		return this.detectWorkspaceFromModuleBtn.getSelection();
	}
	
	public String getWorkspacePath() {
		return this.workspacePath.getText();
	}
	
	private boolean moduleDoesNotExist() {
		IPath modulePath = new Path(infLocationPath.getText().toString());
		return !(modulePath.toFile().exists());
	}

	public void modifyText(ModifyEvent e) {
		if(!infLocationPath.getText().toString().isEmpty() && moduleDoesNotExist()) {
			this.setErrorMessage("The passed module does not exist!");
		} else {
			try {
				workspacePath.setText(Edk2Module.inferWorkspaceFromElementPath(infLocationPath.getText()));
				this.setErrorMessage(null);
				this.setPageComplete(true);
				
				return ;
			} catch(IllegalArgumentException ex) {
				this.setErrorMessage("Could not infer workspace for the selected module.");
			}
			
		}
		this.setPageComplete(false);
	}
}
