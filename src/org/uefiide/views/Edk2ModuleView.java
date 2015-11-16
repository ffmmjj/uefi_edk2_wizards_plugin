package org.uefiide.views;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.uefiide.structures.Edk2Module;

public class Edk2ModuleView extends ViewPart {
	List<IProject> edk2ModuleProjects;
	TreeViewer tree;

	public Edk2ModuleView() {
		edk2ModuleProjects = new LinkedList<IProject>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		for(IProject project : projects) {
			edk2ModuleProjects.add(project);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		
		
		List<Edk2Module> modules = new LinkedList<>();
		try {
			for(IProject project : edk2ModuleProjects) {
				String modulePath = project.getPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "MODULE_ROOT_PATH")) + "/" + project.getName() + ".inf";
				String workspacePath = project.getPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "EDK2_WORKSPACE"));
				
				modules.add(new Edk2Module(modulePath, workspacePath));
			}
			
			tree = new TreeViewer(parent);
			tree.setContentProvider(new Edk2ModuleContentProvider(modules.toArray(new Edk2Module[modules.size()])));
			tree.setLabelProvider(new Edk2ModuleLabelProvider());
			tree.setInput(modules.toArray(new Edk2Module[modules.size()]));
			//tree.setInput(new String[]{"parent1", "parent2"});
			tree.expandAll();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setFocus() {
		tree.getControl().setFocus();
	}
}
