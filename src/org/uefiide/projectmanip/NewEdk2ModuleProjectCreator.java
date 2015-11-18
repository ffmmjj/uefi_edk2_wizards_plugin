package org.uefiide.projectmanip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.Edk2Module;

public class NewEdk2ModuleProjectCreator {
	public static void CreateNewEdk2ModuleProject(String moduleFolder, String moduleName, String workspace, IProgressMonitor monitor) {
		try {
			IPath moduleFolderPath = new Path(moduleFolder);
			IPath moduleLocationPath = moduleFolderPath.append(moduleName);
			
			File infRootFolder = new File(moduleFolderPath.toString());
			if(!infRootFolder.exists()) {
				infRootFolder.mkdirs();
			}
			
			File infFile = new File(moduleLocationPath.toString());
			infFile.createNewFile();
			FileOutputStream fop = new FileOutputStream(infFile);
			populateInfContents(fop);
			fop.close();
			
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

	private static void populateInfContents(FileOutputStream fop) throws IOException {
		fop.write("[Defines]\n".getBytes());
		fop.write("  MODULE_TYPE = UEFI_APPLICATION\n".getBytes());
		fop.write("  INF_VERSION = 0x00010006\n".getBytes());
		fop.write("  BASE_NAME = TestModule\n".getBytes());
		fop.write("  VERSION_STRING = 0.1\n".getBytes());
		fop.write("  FILE_GUID = a912f198-7f0e-4803-b918-b757b80cec83\n".getBytes());
		fop.write("  ENTRY_POINT = ShellCEntryLib\n".getBytes());
		fop.write("[Sources]\n".getBytes());
		fop.write("  TestModule.c\n".getBytes());
		fop.write("[Packages]\n".getBytes());
		fop.write("  MdePkg/MdePkg.dec\n".getBytes());
		fop.write("  ShellPkg/ShellPkg.dec\n".getBytes());
		fop.write("[LibraryClasses]\n".getBytes());
		fop.write("  UefiLib\n".getBytes());
		fop.write("  ShellCEntryLib\n".getBytes());
	}
}
