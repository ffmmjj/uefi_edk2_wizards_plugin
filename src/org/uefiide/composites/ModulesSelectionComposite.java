package org.uefiide.composites;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.uefiide.structures.Edk2Module;

public class ModulesSelectionComposite extends Composite {

	private List<Button> moduleBtns = new LinkedList<>();
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ModulesSelectionComposite(Composite parent, int style, List<Edk2Module> modules) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setText("Choose the modules to be included in the project");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setExpandHorizontal(true);
		
		Composite scrollParent = new Composite(scrolledComposite, SWT.NULL);
		scrollParent.setLayout(new FillLayout(SWT.VERTICAL));
		scrollParent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fillModulesListWindow(scrollParent, modules);
		
		scrolledComposite.setContent(scrollParent);
		
		final Button btnCheckButton = new Button(this, SWT.CHECK);
		btnCheckButton.setText("Select all");
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
		    public void widgetSelected(SelectionEvent e)
		    {
		        for(Button btn : moduleBtns) {
		        	btn.setSelection(btnCheckButton.getSelection());
		        }
		    }
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void fillModulesListWindow(Composite window, List<Edk2Module> modules) {
		for(Edk2Module module : modules) {
			Button moduleCheckBtn = new Button(window, SWT.CHECK);
			moduleCheckBtn.setText(module.getName() + " (" + module.getPath() + ")");
			moduleCheckBtn.setData("module_name", module.getName());
			moduleCheckBtn.setData("module_path", module.getPath());
			moduleBtns.add(moduleCheckBtn);
		}
	}
	
	public List<Edk2Module> getSelectedModules() {
		List<Edk2Module> selectedModules = new LinkedList<>();
		
		for(Button btn : moduleBtns) {
			if(btn.getSelection()) {
				selectedModules.add(
						new Edk2Module(	(String)btn.getData("module_name"), 
										(String)btn.getData("module_path"))
						);
			}
		}
		return selectedModules;
	}

}
