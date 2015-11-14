package org.uefiide.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
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
		
		/*
		for(IProject project : projects) {
			if(isEdk2ModuleProject(project)) {
				edk2ModuleProjects.add(project);
			}
		}
		*/
	}

	private void addModuleStructureToView(Edk2Module module, TreeViewer tree) {
		// TODO Auto-generated method stub
		
	}

	private boolean isEdk2ModuleProject(IProject project) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		tree = new TreeViewer(parent);
		tree.setContentProvider(new Edk2ModuleContentProvider(null));
		tree.setLabelProvider(new Edk2ModuleLabelProvider());
		tree.setInput(new String[]{"parent1", "parent2"});
		tree.expandAll();
		
		/*
		for(IProject project : edk2ModuleProjects) {
			IFile moduleInf = project.getFile(project.getName() + ".inf");
			Edk2Module module = new Edk2Module(moduleInf.getFullPath().toString());
			addModuleStructureToView(module, tree);
		}
		*/

	}

	@Override
	public void setFocus() {
		tree.getControl().setFocus();
	}
}
