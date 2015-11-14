package org.uefiide.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.uefiide.structures.Edk2Module;

public class Edk2ModuleContentProvider implements ITreeContentProvider {
	private Edk2Module[] modules;

	public Edk2ModuleContentProvider(Edk2Module[] edk2Modules) {
		this.modules = edk2Modules;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		//return this.modules;
		return new String[]{"parent1", "parent2"};
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		//Edk2Module module = (Edk2Module) parentElement;
		String parentStr = (String) parentElement;
		if(parentStr.equals("parent1")) {
			return new String[]{"child11", "child12"};
		} else if(parentStr.equals("parent2")) {
			return new String[]{"child21", "child22"};
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		String elementStr = (String) element;
		switch(elementStr) {
		case "child11":
		case "child12":
			return "parent1";
			
		case "child21":
		case "child22":
			return "parent2";
		}
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		String elementStr = (String) element;
		return elementStr.startsWith("parent");
	}

}
