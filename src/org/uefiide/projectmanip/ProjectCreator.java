package org.uefiide.projectmanip;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.Edk2Module;

public class ProjectCreator {
	public static IProject createEdk2Project(String name, URI location, List<Edk2Module> modules) throws CoreException, BuildException {
		IProject newEdk2Project = createBaseProject(name, location);
		
		addToProjectStructure(newEdk2Project, modules);
		
		CProjectNature.addCNature(newEdk2Project, null);
		
		return newEdk2Project;
	}
	
	private static IProject createBaseProject(String projectName, URI location) {
        // it is acceptable to use the ResourcesPlugin class
        IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
 
        if (!newProject.exists()) {
            URI projectLocation = location;
            IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());
            if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
                projectLocation = null;
            }
 
            desc.setLocationURI(projectLocation);
            try {
                newProject.create(desc, null);
                if (!newProject.isOpen()) {
                    newProject.open(null);
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
 
        return newProject;
    }
	
	private static void createFolder(IFolder folder) throws CoreException {
        IContainer parent = folder.getParent();
        if (parent instanceof IFolder) {
            createFolder((IFolder) parent);
        }
        if (!folder.exists()) {
        	folder.create(IResource.VIRTUAL, false, null);
        }
    }
 
    /**
     * Create a folder structure with a parent root, overlay, and a few child
     * folders.
     *
     * @param newProject
     * @param paths
     * @throws CoreException
     */
    private static void addToProjectStructure(IProject newProject, List<Edk2Module> modules) {
        for (Edk2Module module : modules) {
        	IFolder srcFolder = newProject.getFolder(module.getElementPath().substring(0, module.getElementPath().lastIndexOf('/')));
        	try {
				createFolder(srcFolder);
			         	
				IFile infFile  = srcFolder.getFile(module.getName() + ".inf");
				System.out.println(newProject.getLocation() + module.getElementPath());
				System.out.println(module.getElementPath());
				String edk2Location = "/home/felipe/dev/edk2stub/";
				infFile.createLink(new Path(edk2Location + module.getElementPath()), IResource.VIRTUAL, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
