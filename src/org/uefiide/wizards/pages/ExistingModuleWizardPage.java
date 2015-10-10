package org.uefiide.wizards.pages;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.*;

public class ExistingModuleWizardPage extends WizardPage {
	private Button browse;
	private Text prjName;
	private Text locTxt;

	public ExistingModuleWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Opens an EDK2 module project");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setBounds(15, 25, 300, 400);

		Label loc = new Label(container,SWT.NONE);
		loc.setText("Enter the module's .inf location:");
		loc.setBounds(50,90,150,27);

		locTxt = new Text(container,SWT.BOLD | SWT.BORDER);
		locTxt.setBounds(220, 90, 170, 27);

		browse = new Button(container,SWT.NONE);
		browse.setText("Browse");
		browse.setBounds(395, 90, 80, 27);

		addListeners();

		setControl(container);
	}

	private void addListeners() {
		browse.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog fileDialog = new FileDialog(browse.getShell());
				fileDialog.setText("Select the parent directory for project");
				String path = fileDialog.open();
				locTxt.setText(path);
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
		return locTxt.getText();
	}

}
