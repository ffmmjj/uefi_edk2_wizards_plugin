package org.uefiide.projectmanip;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.uefiide.projectmanip.internals.ProjectSettingsManager;
import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.Edk2Package;

public class ProjectStructureUpdater {

	public static void createResourceParentInFileSystem(IPath resourceAbsolutePath) {
		// Create resource's parent if it doesn't exist in the file system
		File resourceParent = new File(resourceAbsolutePath.removeLastSegments(1).toString());
		resourceParent.mkdirs();
	}

	public static void AddModuleResourceToProject(IProject project, String resourceRelativePathString) throws CoreException, IOException {
		String projectLocation = project.getPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "MODULE_ROOT_PATH"));
		IPath resourceRelativePath = new Path(resourceRelativePathString);
		IPath resourceAbsolutePath = new Path(projectLocation).append(resourceRelativePath);
	
		if(resourceRelativePath.segmentCount() > 1) {
			IContainer currentFolder = project;
			IPath currentAbsolutePath = new Path(projectLocation);
	
			createResourceParentInFileSystem(resourceAbsolutePath);
	
			for(String segment : resourceRelativePath.removeLastSegments(1).segments()) {
				currentFolder = currentFolder.getFolder(new Path(segment));
				currentAbsolutePath = currentAbsolutePath.append(segment);
	
				if(!currentFolder.exists()) {
					((IFolder)currentFolder).createLink(currentAbsolutePath, IResource.VIRTUAL, null);
				}
			}
		}
	
		File resourceInFileSystem = new File(resourceAbsolutePath.toString());
		if(!resourceInFileSystem.exists()) {
			resourceInFileSystem.createNewFile();
		}
	
		IFile file = project.getFile(resourceRelativePath);
		if(!file.exists()) {
			file.createLink(resourceAbsolutePath, IResource.VIRTUAL, null);
		}
	}

	public static void addNewSources(IProject project, Edk2Module newModule) throws CoreException, IOException {
		for(String source : newModule.getSources()) {
			AddModuleResourceToProject(project, source);
		}
	}

	public static void updateIncludePaths(IProject project, Edk2Module module) {
		List<Edk2Package> modulePackages = module.getParsedPackages();
		List<String> includePaths = new LinkedList<String>();
		for(Edk2Package p : modulePackages) {
			includePaths.addAll(p.getAbsoluteIncludePaths());
		}
		ProjectSettingsManager.setIncludePaths(project, includePaths);
	}

	public static void UpdateProjectStructureFromModule(IProject project, Edk2Module module) {
		ProjectStructureUpdater.UpdateProjectStructureFromModuleDiff(project, null, module);
	}

	public static void UpdateProjectStructureFromModuleDiff(IProject project, Edk2Module oldModule, Edk2Module newModule) {
		try {
			removeOldSources(project, oldModule, newModule);
			addNewSources(project, newModule);
			AddModuleResourceToProject(project, new Path(newModule.getElementPath()).lastSegment().toString());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void removeOldSources(IProject project, Edk2Module oldModule, Edk2Module newModule)
			throws CoreException {
		if(oldModule != null) {
			Set<String> oldSources = new HashSet<>();
			oldSources.addAll(oldModule.getSources());
			oldSources.removeAll(newModule.getSources());
	
			for(String oldSource : oldSources) {
				IFile fileToRemove = project.getFile(oldSource);
				if(fileToRemove.exists()) {
					fileToRemove.delete(true, null);
				}
			}
		}
	}
}
