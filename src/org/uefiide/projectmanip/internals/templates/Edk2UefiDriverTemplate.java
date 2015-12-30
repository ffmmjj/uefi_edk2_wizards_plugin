package org.uefiide.projectmanip.internals.templates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Path;
import org.uefiide.projectmanip.ModuleProjectCreationContext;
import org.uefiide.projectmanip.internals.Edk2ModuleTemplate;
import org.uefiide.structures.Edk2Module;

public class Edk2UefiDriverTemplate extends Edk2ModuleTemplate {

	public Edk2UefiDriverTemplate(ModuleProjectCreationContext context) {
		super(context);
	}

	@Override
	public void writeModuleTemplate() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String moduleName = context.getModuleName();
		File infFile = new File(new Path(moduleLocation).append(moduleName + ".inf").toString());
		
		if(!infFile.exists()) {
			infFile.createNewFile();
		}
		
		Edk2Module newModule = new Edk2Module(infFile.getAbsolutePath(), context.getWorkspaceLocation());
	    Map<String, String> defines = newModule.getDefines();
	    List<String> sources = newModule.getSources();
	    List<String> libraries = newModule.getLibraryClasses();
	    List<String> packages = newModule.getEdk2PackageNames();
	    
		defines.put("MODULE_TYPE", "UEFI_DRIVER");
		defines.put("INF_VERSION", "0x00010006");
		defines.put("BASE_NAME", moduleName);
		defines.put("VERSION_STRING", "0.1");
		defines.put("FILE_GUID", UUID.randomUUID().toString());
		defines.put("ENTRY_POINT", moduleName + "EntryPoint\n\n");
		
		sources.add(moduleName + ".c");
		if(context.followsUefiDriverModel()) {
			sources.add("DriverBinding.c");
		}
		
		packages.add("MdePkg/MdePkg.dec");
		
		libraries.add("UefiLib");
		
		newModule.setDefinitions(defines);
		newModule.setPackages(packages);
		newModule.setSources(sources);
		newModule.setLibraryClasses(libraries);
		newModule.save();
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
