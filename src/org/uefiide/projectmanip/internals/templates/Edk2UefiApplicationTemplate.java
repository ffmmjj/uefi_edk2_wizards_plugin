package org.uefiide.projectmanip.internals.templates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.eclipse.core.runtime.Path;
import org.uefiide.projectmanip.ModuleProjectCreationContext;
import org.uefiide.projectmanip.internals.Edk2ModuleTemplate;

public class Edk2UefiApplicationTemplate extends Edk2ModuleTemplate {
	
	public Edk2UefiApplicationTemplate(ModuleProjectCreationContext context) {
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
		writer.write("  MODULE_TYPE = UEFI_APPLICATION\n".getBytes());
		writer.write("  INF_VERSION = 0x00010006\n".getBytes());
		writer.write(("  BASE_NAME = " + moduleName + "\n").getBytes());
		writer.write("  VERSION_STRING = 0.1\n".getBytes());
		writer.write(("  FILE_GUID = " + UUID.randomUUID().toString() + "\n").getBytes());
		writer.write("  ENTRY_POINT = ShellCEntryLib\n".getBytes());
		writer.write("[Sources]\n".getBytes());
		writer.write(("  " + moduleName + ".c\n").getBytes());
		writer.write("[Packages]\n".getBytes());
		writer.write("  MdePkg/MdePkg.dec\n".getBytes());
		writer.write("  ShellPkg/ShellPkg.dec\n".getBytes());
		writer.write("[LibraryClasses]\n".getBytes());
		writer.write("  UefiLib\n".getBytes());
		writer.write("  ShellCEntryLib\n".getBytes());
		
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
		writer.write("#include  <Library/ShellCEntryLib.h>\n".getBytes());
		writer.write("\n".getBytes());
		writer.write("INTN\n".getBytes());
		writer.write("EFIAPI\n".getBytes());
		writer.write("ShellAppMain (\n".getBytes());
		writer.write("  IN UINTN Argc,\n".getBytes());
		writer.write("  IN CHAR16 **Argv\n".getBytes());
		writer.write("  )\n".getBytes());
		writer.write("{\n".getBytes());
		writer.write("  Print(L\"Hello there fellow Programmer.\\n\");\n".getBytes());
		writer.write("  Print(L\"Welcome to the world of EDK II.\\n\");\n".getBytes());
		writer.write("\n".getBytes());
		writer.write("  return(0);\n".getBytes());
		writer.write("}\n".getBytes());
		
		writer.close();
	}
}
