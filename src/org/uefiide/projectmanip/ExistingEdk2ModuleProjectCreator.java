package org.uefiide.projectmanip;

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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
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
import org.uefiide.structures.Edk2Module;

@SuppressWarnings("restriction")
public class ExistingEdk2ModuleProjectCreator {

	public static void createEDK2ProjectFromExistingModule(Edk2Module module, IProgressMonitor monitor) throws CoreException {
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
		ExistingEdk2ModuleProjectCreator.ConfigureProjectNature(newProjectHandle);
		monitor.beginTask("Creating project structure", 45);
		ProjectStructureUpdater.UpdateProjectStructureFromModule(newProjectHandle, module);

		monitor.beginTask("Parsing include paths", 65);
		ProjectStructureUpdater.updateIncludePaths(newProjectHandle, module);

		monitor.beginTask("Saving EDK2 project properties", 95);

		setResourceChangeListeners(newProjectHandle);
	}

	public static void setResourceChangeListeners(IProject newProjectHandle) {
		Edk2ModuleObservablesManager.getProjectModuleModificationObservable()
		.filter(event -> event.getProject() == newProjectHandle)
		.map(ev -> {
			return new WorkspaceJob("Updating project"){
				@Override 
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					Edk2Module module = ev.getNewModule();

					ProjectStructureUpdater.updateIncludePaths(ev.getProject(), module);
					ProjectStructureUpdater.UpdateProjectStructureFromModuleDiff(ev.getProject(), ev.getOldModule(), module);
					ev.getProject().refreshLocal(IResource.DEPTH_INFINITE,monitor);

					return Status.OK_STATUS;
				}
			};
		})
		.subscribe(job -> job.schedule());
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
}
