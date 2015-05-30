package customplugin.wizards;

import java.net.URI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import customplugin.CustomProjectSupport;

public class CustomProjectNewWizard extends Wizard implements INewWizard {
	private WizardNewProjectCreationPage _pageOne;
	
	public CustomProjectNewWizard() {
		setWindowTitle("EDK2 Project Wizard");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    CustomProjectSupport.createEdk2Project(name, location);
	 
	    return true;
	}

	@Override
	 public void addPages() {
		 super.addPages();
	
		 _pageOne = new WizardNewProjectCreationPage("EDK2 Project Wizard");
		 _pageOne.setTitle("EDK2 Project");
		 _pageOne.setDescription("A new EDK2 based project");
	
		 addPage(_pageOne);
	 }
}
