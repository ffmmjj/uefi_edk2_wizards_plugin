package org.uefiide.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
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
	
	Group moduleTypeGroup;
	Button UefiApplicationRadioBtn;
	Button UefiStdLibApplicationRadioBtn;
	Button UefiDriverApplicationRadioBtn;
	Button UefiLibraryRadioBtn;
	
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
		parent.setSize(900, 900);
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));

		Label moduleRootLabel = new Label(parent, SWT.BORDER);
		moduleRootLabel.setText("Enter the module's .inf location:");
		Composite moduleRootContainer = new Composite(container, SWT.BORDER);
		moduleRootContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		moduleRootContainer.setLayout(new GridLayout(2, false));
		moduleRootFolder = new Text(moduleRootContainer,SWT.BOLD | SWT.BORDER);
		moduleRootFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		moduleRootFolderBtn = new Button(moduleRootContainer,SWT.BORDER);
		moduleRootFolderBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		moduleRootFolderBtn.setText("Browse");

		detectWorkspaceFromModuleBtn = new Button(container, SWT.CHECK);
		detectWorkspaceFromModuleBtn.setText("Detect workspace from module path");
		detectWorkspaceFromModuleBtn.setSelection(true);
		detectWorkspaceFromModuleBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				NewEdk2ModuleProjectPage.this.workspacePathLabel.setEnabled(!shouldInferWorkspacePath());
				NewEdk2ModuleProjectPage.this.workspacePath.setEnabled(!shouldInferWorkspacePath());
				NewEdk2ModuleProjectPage.this.workspacePathBtn.setEnabled(!shouldInferWorkspacePath());
			}
		});
		
		createWorkspaceFormInput(container);
		
		createModuleTypeRadioGroup(container);
	    
		addListeners();

		setControl(container);
	}

	private void createWorkspaceFormInput(Composite container) {
		workspacePathLabel = new Label(container, SWT.BORDER);
		workspacePathLabel.setText("Enter the workspace path:");
		workspacePathLabel.setEnabled(false);
		
		SashForm form = new SashForm(container, SWT.HORIZONTAL | SWT.BORDER);
		form.setLayout(new GridLayout(1, false));
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite workspacePathComposite = new Composite(form, SWT.BORDER);
		workspacePathComposite.setLayout(new GridLayout(1, true));
		workspacePathComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		workspacePath = new Text(workspacePathComposite, SWT.PUSH | SWT.BOLD | SWT.BORDER);
		workspacePath.setEnabled(false);
		workspacePath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite workspacePathBtnComposite = new Composite(form, SWT.BORDER);
		workspacePathBtnComposite.setLayout(new GridLayout());
		workspacePathBtnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		workspacePathBtn = new Button(workspacePathBtnComposite, SWT.PUSH);
		workspacePathBtn.setText("Browse");
		workspacePathBtn.setEnabled(false);
		workspacePathBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		form.setWeights(new int[]{7, 1});
	}

	private void createModuleTypeRadioGroup(Composite container) {
		moduleTypeGroup = new Group(container, SWT.SHADOW_IN);
		moduleTypeGroup.setText("Module type");
		moduleTypeGroup.setLayout(new RowLayout(SWT.VERTICAL));
	    UefiApplicationRadioBtn = new Button(moduleTypeGroup, SWT.RADIO);
	    UefiApplicationRadioBtn.setText("UEFI Application");
	    UefiStdLibApplicationRadioBtn = new Button(moduleTypeGroup, SWT.RADIO);
	    UefiStdLibApplicationRadioBtn.setText("UEFI StdLib Application");
	    UefiDriverApplicationRadioBtn = new Button(moduleTypeGroup, SWT.RADIO);
	    UefiDriverApplicationRadioBtn.setText("UEFI Driver");
	    UefiLibraryRadioBtn = new Button(moduleTypeGroup, SWT.RADIO);
	    UefiLibraryRadioBtn.setText("UEFI Library");
	    
	    UefiApplicationRadioBtn.setData(Edk2ModuleType.UEFI_APPLICATION);
	    UefiStdLibApplicationRadioBtn.setData(Edk2ModuleType.UEFI_STDLIB_APPLICATION);
	    UefiDriverApplicationRadioBtn.setData(Edk2ModuleType.UEFI_DRIVER);
	    UefiLibraryRadioBtn.setData(Edk2ModuleType.LIBRARY_CLASS_IMPLEMENTATION);
	    
	    UefiApplicationRadioBtn.setSelection(true);
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
	
	public Edk2ModuleType getSelectedModuleType() {
		for(Control child: moduleTypeGroup.getChildren()) {
			Button childBtn = (Button) child;
			
			if(childBtn.getSelection()) {
				return (Edk2ModuleType)childBtn.getData();
			}
		}
		
		return null;
	}
}
