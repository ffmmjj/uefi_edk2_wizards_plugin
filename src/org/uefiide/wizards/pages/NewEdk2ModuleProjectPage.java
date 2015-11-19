package org.uefiide.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.uefiide.structures.Edk2Module.Edk2ModuleType;

public class NewEdk2ModuleProjectPage extends WizardPage {
	Text moduleName;
	Text moduleRootFolder;
	Text workspacePath;
	Edk2ModuleType moduleType;
	
	Label workspacePathLabel;
	Button workspacePathBtn;
	Button moduleRootFolderBtn;
	Button detectWorkspaceFromModuleBtn;
	
	/**
	 * Create the wizard.
	 */
	public NewEdk2ModuleProjectPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Creates a new EDK2 Module Project");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		//TODO use layouts instead of absolute positions
		boolean detectWorkspaceFromModule = true;

		Composite container = new Composite(parent, SWT.NULL);
		container.setBounds(15, 25, 300, 400);

		Label moduleRootLabel = new Label(container,SWT.NONE);
		moduleRootLabel.setText("Enter the module's .inf location:");
		moduleRootLabel.setBounds(50, 95, 150, 27);

		moduleRootFolder = new Text(container,SWT.BOLD | SWT.BORDER);
		moduleRootFolder.setBounds(220, 90, 170, 27);

		moduleRootFolderBtn = new Button(container,SWT.NONE);
		moduleRootFolderBtn.setText("Browse");
		moduleRootFolderBtn.setBounds(395, 90, 80, 27);

		detectWorkspaceFromModuleBtn = new Button(container, SWT.CHECK);
		detectWorkspaceFromModuleBtn.setText("Detect workspace from module path");
		detectWorkspaceFromModuleBtn.setBounds(50, 120, 350, 27);
		detectWorkspaceFromModuleBtn.setSelection(detectWorkspaceFromModule);
		detectWorkspaceFromModuleBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				NewEdk2ModuleProjectPage.this.workspacePathLabel.setEnabled(!shouldInferWorkspacePath());
				NewEdk2ModuleProjectPage.this.workspacePath.setEnabled(!shouldInferWorkspacePath());
				NewEdk2ModuleProjectPage.this.workspacePathBtn.setEnabled(!shouldInferWorkspacePath());
			}
		});

		workspacePathLabel = new Label(container,SWT.NONE);
		workspacePathLabel.setText("Enter the workspace path:");
		workspacePathLabel.setBounds(30, 160 ,185, 27);
		workspacePathLabel.setEnabled(!detectWorkspaceFromModule);

		workspacePath = new Text(container,SWT.BOLD | SWT.BORDER);
		workspacePath.setBounds(220, 150, 170, 27);
		workspacePath.setEnabled(!detectWorkspaceFromModule);

		workspacePathBtn = new Button(container,SWT.NONE);
		workspacePathBtn.setText("Browse");
		workspacePathBtn.setBounds(395, 150, 80, 27);
		workspacePathBtn.setEnabled(!detectWorkspaceFromModule);

		addListeners();

		setControl(container);
	}
	
	private void addListeners() {
		moduleRootFolderBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
				DirectoryDialog dirDialog = new DirectoryDialog(moduleRootFolderBtn.getShell());
				dirDialog.setText("Select the parent directory for project");
				String path = dirDialog.open();
				if(path != null) {
					moduleRootFolder.setText(path);
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
	}
	
	public boolean shouldInferWorkspacePath() {
		return this.detectWorkspaceFromModuleBtn.getSelection();
	}
	public String getNewModuleRootFolder(){
		return moduleRootFolder.getText();
	}
	public String getWorkspace(){
		return workspacePath.getText();
	}
}
