package org.uefiide.projectmanip;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class Edk2ModuleProjectCreator {

	public static void CreateProjectStructure(IProject project, String location) {
		try {
			addToProject(project, project, location, "");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static void addToProject(IProject project, IContainer parentFolder, String location, String node) throws CoreException{
		File nodeFile = new File(location + "/" + node);

		if(nodeFile.isDirectory()){
			IContainer srcFolder;
			if(node != null && !node.isEmpty()) {
				IFolder nodeFolder = parentFolder.getFolder(new Path(node));
				nodeFolder.create(IResource.VIRTUAL, false, null);
				srcFolder = nodeFolder;
			} else {
				srcFolder = parentFolder;
			}
			String[] subDirs = nodeFile.list();

			for(String filename : subDirs){
				if(!filename.equals(".project")) {
					addToProject(project, srcFolder, nodeFile.getAbsolutePath(), filename);
				}
			}
		} else if(nodeFile.isFile()) {
			IFile infFile  = parentFolder.getFile(new Path(node));
			infFile.createLink(new Path(location).append(node), IResource.VIRTUAL, null);
		}
	}
}
