package org.uefiide.wizards.pages;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ExistingModuleWizardPage extends WizardPage {
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
		//TODO use layouts instead of absolute positions
		boolean detectWorkspaceFromModule = true;
		
		Composite container = new Composite(parent, SWT.NULL);
		container.setBounds(15, 25, 300, 400);

		Label infPathLabel = new Label(container,SWT.NONE);
		infPathLabel.setText("Enter the module's .inf location:");
		infPathLabel.setBounds(50, 95, 150, 27);

		infLocationPath = new Text(container,SWT.BOLD | SWT.BORDER);
		infLocationPath.setBounds(220, 90, 170, 27);

		infBrowserBtn = new Button(container,SWT.NONE);
		infBrowserBtn.setText("Browse");
		infBrowserBtn.setBounds(395, 90, 80, 27);
		
		detectWorkspaceFromModuleBtn = new Button(container, SWT.CHECK);
		detectWorkspaceFromModuleBtn.setText("Detect workspace from module path");
		detectWorkspaceFromModuleBtn.setBounds(50, 120, 350, 27);
		detectWorkspaceFromModuleBtn.setSelection(detectWorkspaceFromModule);
		detectWorkspaceFromModuleBtn.addSelectionListener(new SelectionAdapter() {
			@Override
		    public void widgetSelected(SelectionEvent e)
		    {
				ExistingModuleWizardPage.this.workspacePathLabel.setEnabled(!shouldInferWorkspacePath());
				ExistingModuleWizardPage.this.workspacePath.setEnabled(!shouldInferWorkspacePath());
				ExistingModuleWizardPage.this.workspacePathBtn.setEnabled(!shouldInferWorkspacePath());
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
}
