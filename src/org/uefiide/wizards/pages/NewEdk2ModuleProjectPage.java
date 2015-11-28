package org.uefiide.wizards.pages;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class NewEdk2ModuleProjectPage extends WizardPage implements ModifyListener {
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
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));

		createModuleNameFormInput(container);
		createModuleLocationFormInput(container);
		createWorkspaceFormInput(container);
		createModuleTypeRadioGroup(container);
	    
		addListeners();

		setControl(container);
	}

	private void createModuleNameFormInput(Composite container) {
		Label moduleNameLabel = new Label(container, SWT.NULL);
		moduleNameLabel.setText("Enter the module's name:");
		
		moduleName = new Text(container, SWT.BOLD | SWT.BORDER);
		moduleName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	private void createModuleLocationFormInput(Composite container) {
		Label moduleRootLabel = new Label(container, SWT.NULL);
		moduleRootLabel.setText("Enter the module's .inf location:");
		
		SashForm form = new SashForm(container, SWT.HORIZONTAL | SWT.NULL);
		form.setLayout(new GridLayout(1, false));
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		form.setSashWidth(0);
		
		Composite moduleRootFolderTxtContainer = new Composite(form, SWT.NULL);
		moduleRootFolderTxtContainer.setLayout(new GridLayout(1, false));
		moduleRootFolderTxtContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		moduleRootFolder = new Text(moduleRootFolderTxtContainer, SWT.BOLD | SWT.BORDER);
		moduleRootFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite moduleRootFolderBtnContainer = new Composite(form, SWT.NULL);
		moduleRootFolderBtnContainer.setLayout(new GridLayout(1, false));
		moduleRootFolderBtnContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		moduleRootFolderBtn = new Button(moduleRootFolderBtnContainer, SWT.PUSH);
		moduleRootFolderBtn.setText("Browse");
		moduleRootFolderBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
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

	private void createModuleTypeRadioGroup(Composite container) {
		moduleTypeGroup = new Group(container, SWT.SHADOW_IN);
		moduleTypeGroup.setText("Module type");
		moduleTypeGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
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
		detectWorkspaceFromModuleBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				NewEdk2ModuleProjectPage.this.workspacePathLabel.setEnabled(!shouldInferWorkspacePath());
				NewEdk2ModuleProjectPage.this.workspacePath.setEnabled(!shouldInferWorkspacePath());
				NewEdk2ModuleProjectPage.this.workspacePathBtn.setEnabled(!shouldInferWorkspacePath());
			}
		});
		moduleName.addModifyListener(this);
		moduleRootFolder.addModifyListener(this);
		UefiLibraryRadioBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewEdk2ModuleProjectPage.this.setPageComplete(NewEdk2ModuleProjectPage.this.shouldCompletePage() && UefiLibraryRadioBtn.getSelection());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	public boolean shouldInferWorkspacePath() {
		return this.detectWorkspaceFromModuleBtn.getSelection();
	}
	public String getNewModuleName() {
		return moduleName.getText();
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

	private boolean moduleRootFolderExists() {
		return (new File(moduleRootFolder.getText()).exists()); 
	}
	
	private boolean moduleAlreadyExists() {
		IPath modulePath = new Path(moduleRootFolder.getText()).append(moduleName.getText() + ".inf");
		return modulePath.toFile().exists();
	}
	
	private boolean shouldCompletePage() {
		return 	!moduleName.getText().isEmpty() &&
				!moduleRootFolder.getText().isEmpty() &&
				moduleRootFolderExists() && 
				!moduleAlreadyExists();
	}
	
	@Override
	public void modifyText(ModifyEvent e) {
		this.setPageComplete(this.shouldCompletePage());
		
		if(moduleRootFolder.getText().isEmpty()) {
			this.setErrorMessage(null);
		} else if(!moduleRootFolderExists()) {
			this.setErrorMessage("The selected module location does not exist.");
		} else if(!moduleName.getText().isEmpty() && moduleAlreadyExists()) {
			this.setErrorMessage("A module with the same name already exists in the selected root folder.");
		} else {
			this.setErrorMessage(null);
		}
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return 	shouldCompletePage() && 
				(UefiLibraryRadioBtn.getSelection() || UefiDriverApplicationRadioBtn.getSelection());
	};
	
	@Override
	public IWizardPage getNextPage() {
		if(UefiDriverApplicationRadioBtn.getSelection()) {
			return this.getWizard().getPage("UefiDriverPage");
		} else if(UefiLibraryRadioBtn.getSelection()){
			return this.getWizard().getPage("NewLibraryClassPage");
		}
		
		return null;
	}
}
