package org.uefiide.projectmanip.internals.templates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.eclipse.core.runtime.Path;
import org.uefiide.projectmanip.ModuleProjectCreationContext;
import org.uefiide.projectmanip.internals.Edk2ModuleTemplate;

public class Edk2UefiDriverTemplate extends Edk2ModuleTemplate {

	public Edk2UefiDriverTemplate(ModuleProjectCreationContext context) {
		super(context);
	}

	@Override
	public void writeModuleTemplate() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String moduleName = context.getModuleName();
		File infFile = new File(new Path(moduleLocation).append(moduleName + ".inf").toString());
		FileOutputStream writer = new FileOutputStream(infFile);
		
		if(!infFile.exists()) {
			infFile.createNewFile();
		}
		
		writer.write("[Defines]\n".getBytes());
		writer.write("  MODULE_TYPE = UEFI_DRIVER\n".getBytes());
		writer.write("  INF_VERSION = 0x00010006\n".getBytes());
		writer.write(("  BASE_NAME = " + moduleName + "\n").getBytes());
		writer.write("  VERSION_STRING = 0.1\n".getBytes());
		writer.write(("  FILE_GUID = " + UUID.randomUUID().toString() + "\n").getBytes());
		writer.write(("  ENTRY_POINT = " + moduleName + "EntryPoint\n\n").getBytes());
		writer.write("[Sources]\n".getBytes());
		writer.write(("  " + moduleName + ".c\n").getBytes());
		if(context.followsUefiDriverModel()) {
			writer.write("  DriverBinding.c\n\n".getBytes());
		}
		writer.write("[Packages]\n".getBytes());
		writer.write("  MdePkg/MdePkg.dec\n\n".getBytes());
		writer.write("[LibraryClasses]\n".getBytes());
		writer.write("  UefiLib\n".getBytes());
		
		writer.close();
	}

	@Override
	public void writeSourcesTemplate() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String moduleName = context.getModuleName();
		File appSource = new File(new Path(moduleLocation).append(moduleName + ".c").toString());
		FileOutputStream writer = new FileOutputStream(appSource);
		
		if(!appSource.exists()) {
			appSource.createNewFile();
		}
		
		writer.write("#include <Uefi.h>\n".getBytes());
		writer.write("#include <Library/UefiLib.h>\n".getBytes());
		writer.write("\n".getBytes());
		writer.write("EFI_STATUS\n".getBytes());
		writer.write("EFIAPI\n".getBytes());
		writer.write((moduleName + "EntryPoint (\n").getBytes());
		writer.write("  IN EFI_HANDLE        ImageHandle,\n".getBytes());
		writer.write("  IN EFI_SYSTEM_TABLE  *SystemTable\n".getBytes());
		writer.write("  )\n".getBytes());
		writer.write("{\n".getBytes());
		writer.write("  /// Install your protocols here...\n".getBytes());
		writer.write("\n".getBytes());
		writer.write("  return EFI_SUCCESS;\n".getBytes());
		writer.write("}\n".getBytes());
		
		writer.close();
		
		if(context.followsUefiDriverModel()) {
			writeDriverBindingSourceFile();
		}
	}
	
	private void writeDriverBindingSourceFile() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String fileName = "DriverBinding.c";
		File appSource = new File(new Path(moduleLocation).append(fileName).toString());
		FileOutputStream writer = new FileOutputStream(appSource);
		
		if(!appSource.exists()) {
			appSource.createNewFile();
		}
		
		writer.write("#include <Uefi.h>\n".getBytes());
		writer.write("#include <Library/UefiLib.h>\n".getBytes());
		writer.write("#include <Protocol/DriverBinding.h>\n".getBytes());
		writer.write("\n".getBytes());
		writer.write("/// Define the Start(), Stop() and Supported() functions here...".getBytes());
		writer.write("\n".getBytes());
		
		writer.close();
	}

}
