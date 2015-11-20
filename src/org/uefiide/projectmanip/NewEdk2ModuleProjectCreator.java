package org.uefiide.projectmanip;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.uefiide.projectmanip.internals.Edk2ModuleTemplate;
import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.Edk2Module.Edk2ModuleType;

public class NewEdk2ModuleProjectCreator {
	public static void CreateNewEdk2ModuleProject(String moduleFolder, String moduleName, String workspace, 
			Edk2ModuleType type, IProgressMonitor monitor) {
		try {
			IPath moduleFolderPath = new Path(moduleFolder);
			IPath moduleLocationPath = moduleFolderPath.append(moduleName + ".inf");
			
			Edk2ModuleTemplate template = Edk2ModuleTemplate.get(type);
			template.createModuleTemplate(moduleFolder, moduleName);
			
			Edk2Module projectModule = new Edk2Module(moduleLocationPath.toString(), workspace);
			ExistingEdk2ModuleProjectCreator.CreateEDK2ProjectFromExistingModule(projectModule, monitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
