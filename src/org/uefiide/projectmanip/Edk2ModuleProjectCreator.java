package org.uefiide.projectmanip;

import java.io.File;
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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.Edk2Package;

public class Edk2ModuleProjectCreator {
	
	public static void CreateEDK2ProjectFromExistingModule(Edk2Module module, IProgressMonitor monitor) throws CoreException {
		IProject newProjectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(module.getName());
		IProjectDescription projDesc;
		newProjectHandle.create(monitor);
		newProjectHandle.open(monitor);
		projDesc = ResourcesPlugin.getWorkspace().newProjectDescription(newProjectHandle.getName());
		IPath newProjectPath = newProjectHandle.getLocation();
		projDesc.setLocation(newProjectPath);
		
		monitor.beginTask("Adding C nature to project", 25);
		CCorePlugin.getDefault().createCDTProject(projDesc, newProjectHandle, null);
		Edk2ModuleProjectCreator.ConfigureProjectNature(newProjectHandle);
		monitor.beginTask("Creating project structure", 45);
		Edk2ModuleProjectCreator.CreateProjectStructure(newProjectHandle, new Path(module.getElementPath()).removeLastSegments(1).toString());
		
		monitor.beginTask("Parsing include paths", 65);
		List<Edk2Package> modulePackages = module.getPackages();
		List<String> includePaths = new LinkedList<String>();
		for(Edk2Package p : modulePackages) {
			includePaths.addAll(p.getAbsoluteIncludePaths());
		}
		monitor.beginTask("Adding include paths", 85);
		ProjectSettingsManager.setIncludePaths(newProjectHandle, includePaths);
		
		ProjectBuildConfigManager.setEDK2BuildCommands(newProjectHandle, null);
		
		monitor.beginTask("Saving EDK2 project properties", 95);
		newProjectHandle.setPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "EDK2_WORKSPACE"), module.getWorkspacePath());
		newProjectHandle.setPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "MODULE_ROOT_PATH"), new Path(module.getElementPath()).removeLastSegments(1).toString());
	}
	
	private static void CreateProjectStructure(IProject project, String location) {
		try {
			addToProject(project, project, location, "");
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

	private static void addToProject(IProject project, IContainer parentFolder, String location, String node) throws CoreException{
		File nodeFile = new File(location + "/" + node);

		if(nodeFile.isDirectory()){
			IContainer srcFolder;
			if(node != null && !node.isEmpty()) {
				IFolder nodeFolder = parentFolder.getFolder(new Path(node));
				nodeFolder.createLink(new Path(location).append(node), IResource.VIRTUAL, null);
				srcFolder = nodeFolder;
			} else {
				srcFolder = parentFolder;
			}
			String[] subDirs = nodeFile.list();

			for(String filename : subDirs){
				if(!filename.equals(".project") && !filename.equals(".cproject")) {
					addToProject(project, srcFolder, nodeFile.getAbsolutePath(), filename);
				}
			}
		} else if(nodeFile.isFile()) {
			IFile infFile  = parentFolder.getFile(new Path(node));
			if(!infFile.exists()) {
				infFile.createLink(new Path(location).append(node), IResource.VIRTUAL, null);
			}
		}
	}
}
