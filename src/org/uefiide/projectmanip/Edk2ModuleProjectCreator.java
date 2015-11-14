package org.uefiide.projectmanip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.cdt.managedbuilder.internal.core.ToolChain;
import org.eclipse.cdt.managedbuilder.ui.wizards.CfgHolder;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.uefiide.events.Edk2ModuleObservablesManager;
import org.uefiide.events.Edk2ModuleObservablesManager.Edk2ModuleChangeEvent;
import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.Edk2Package;

import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

public class Edk2ModuleProjectCreator {
	
	public static void CreateEDK2ProjectFromExistingModule(Edk2Module module, IProgressMonitor monitor) throws CoreException {
		IProject newProjectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(module.getName());
		IProjectDescription projDesc;
		newProjectHandle.create(monitor);
		newProjectHandle.open(monitor);
		projDesc = ResourcesPlugin.getWorkspace().newProjectDescription(newProjectHandle.getName());
		IPath newProjectPath = newProjectHandle.getLocation();
		projDesc.setLocation(newProjectPath);
		
		newProjectHandle.setPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "EDK2_WORKSPACE"), module.getWorkspacePath());
		newProjectHandle.setPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "MODULE_ROOT_PATH"), new Path(module.getElementPath()).removeLastSegments(1).toString());
		monitor.beginTask("Adding C nature to project", 25);
		CCorePlugin.getDefault().createCDTProject(projDesc, newProjectHandle, null);
		Edk2ModuleProjectCreator.ConfigureProjectNature(newProjectHandle);
		monitor.beginTask("Creating project structure", 45);
		Edk2ModuleProjectCreator.UpdateProjectStructureFromModule(newProjectHandle, module);
		
		monitor.beginTask("Parsing include paths", 65);
		updateIncludePaths(newProjectHandle, module);
		
		ProjectBuildConfigManager.setEDK2BuildCommands(newProjectHandle, null);
		
		monitor.beginTask("Saving EDK2 project properties", 95);
		
		setResourceChangeListeners(newProjectHandle);
	}

	public static void setResourceChangeListeners(IProject newProjectHandle) {
		Edk2ModuleObservablesManager.getProjectModuleModificationObservable()
		.filter(new Func1<Edk2ModuleChangeEvent, Boolean>() {
			@Override
			public Boolean call(Edk2ModuleChangeEvent ev) {
				return ev.getProject() == newProjectHandle;
			}
		}).subscribe(new Action1<Edk2ModuleChangeEvent>() {
			@Override
			public void call(Edk2ModuleChangeEvent ev) {
				WorkspaceJob job=new WorkspaceJob("Updating project"){
					@Override 
					public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
						Edk2Module module = ev.getModule();

						Edk2ModuleProjectCreator.updateIncludePaths(ev.getProject(), module);
						Edk2ModuleProjectCreator.UpdateProjectStructureFromModule(ev.getProject(), module);
						ev.getProject().refreshLocal(IResource.DEPTH_INFINITE,monitor);

						return Status.OK_STATUS;
					}
				};

				job.schedule();
			}
		});
	}

	public static void updateIncludePaths(IProject project, Edk2Module module) {
		List<Edk2Package> modulePackages = module.getPackages();
		List<String> includePaths = new LinkedList<String>();
		for(Edk2Package p : modulePackages) {
			includePaths.addAll(p.getAbsoluteIncludePaths());
		}
		ProjectSettingsManager.setIncludePaths(project, includePaths);
	}
	
	public static void UpdateProjectStructureFromModule(IProject project, Edk2Module module) {
		try {
			for(String source : module.getSources()) {
				AddModuleResourceToProject(project, source);
			}
			AddModuleResourceToProject(project, new Path(module.getElementPath()).lastSegment().toString());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void ConfigureProjectNature(IProject project) throws CoreException {
		// Set up build information
		ICProjectDescriptionManager pdMgr = CoreModel.getDefault().getProjectDescriptionManager();
		ICProjectDescription cProjDesc = pdMgr.createProjectDescription(project, false);
		ManagedBuildInfo info = ManagedBuildManager.createBuildInfo(project);
		ManagedProject mProj = new ManagedProject(cProjDesc);
		info.setManagedProject(mProj);

		CfgHolder cfgHolder = new CfgHolder(null, null);
		String s = "0";
		Configuration config = new Configuration(mProj, (ToolChain)null, ManagedBuildManager.calculateChildId(s, null), cfgHolder.getName());
		IBuilder builder = config.getEditableBuilder();
		builder.setManagedBuildOn(false);
		CConfigurationData data = config.getConfigurationData();
		cProjDesc.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, data);

		pdMgr.setProjectDescription(project, cProjDesc);
	}

	private static void AddModuleResourceToProject(IProject project, String resourceRelativePathString) throws CoreException {
		String projectLocation = project.getPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "MODULE_ROOT_PATH"));
		IPath resourceRelativePath = new Path(resourceRelativePathString);
		IPath resourceAbsolutePath = new Path(projectLocation).append(resourceRelativePath);
		
		if(resourceRelativePath.segmentCount() > 1) {
			IContainer currentFolder = project;
			for(String segment : resourceRelativePath.removeLastSegments(1).segments()) {
				currentFolder = currentFolder.getFolder(new Path(segment));
				if(!currentFolder.exists()) {
					IPath currentFolderAbsolutePath = new Path(projectLocation).append(segment);
					((IFolder)currentFolder).createLink(currentFolderAbsolutePath, IResource.VIRTUAL, null);
				}
			}
		}
		
		File resourceInFileSystem = new File(resourceAbsolutePath.toString());
		if(resourceInFileSystem.isFile()) {
			IFile file = project.getFile(resourceRelativePath);
			if(!file.exists()) {
				file.createLink(resourceAbsolutePath, IResource.VIRTUAL, null);
			}
		}
	}
}
