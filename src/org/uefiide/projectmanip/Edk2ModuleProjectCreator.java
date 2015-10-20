package org.uefiide.projectmanip;

import java.io.File;

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
	
	public static void ConfigureProjectNature(IProject project) throws CoreException {
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
