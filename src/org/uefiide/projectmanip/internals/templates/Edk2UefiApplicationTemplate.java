package org.uefiide.projectmanip.internals.templates;

import java.io.FileOutputStream;
import java.io.IOException;

import org.uefiide.projectmanip.internals.Edk2ModuleTemplate;

public class Edk2UefiApplicationTemplate extends Edk2ModuleTemplate {
	private static Edk2UefiApplicationTemplate instance = null;
	
	@Override
	public void writeModuleTemplate(FileOutputStream writer) throws IOException {
		writer.write("[Defines]\n".getBytes());
		writer.write("  MODULE_TYPE = UEFI_APPLICATION\n".getBytes());
		writer.write("  INF_VERSION = 0x00010006\n".getBytes());
		writer.write("  BASE_NAME = TestModule\n".getBytes());
		writer.write("  VERSION_STRING = 0.1\n".getBytes());
		writer.write("  FILE_GUID = a912f198-7f0e-4803-b918-b757b80cec83\n".getBytes());
		writer.write("  ENTRY_POINT = ShellCEntryLib\n".getBytes());
		writer.write("[Sources]\n".getBytes());
		writer.write("  TestModule.c\n".getBytes());
		writer.write("[Packages]\n".getBytes());
		writer.write("  MdePkg/MdePkg.dec\n".getBytes());
		writer.write("  ShellPkg/ShellPkg.dec\n".getBytes());
		writer.write("[LibraryClasses]\n".getBytes());
		writer.write("  UefiLib\n".getBytes());
		writer.write("  ShellCEntryLib\n".getBytes());
	}

	@Override
	public void writeSourceTemplate(FileOutputStream writer) throws IOException {
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
		writer.write("  Print(L\"Hello there fellow Programmer.\n\");\n".getBytes());
		writer.write("  Print(L\"Welcome to the world of EDK II.\n\");\n".getBytes());
		writer.write("\n".getBytes());
		writer.write("  return(0);\n".getBytes());
		writer.write("}\n".getBytes());
	}

	public static Edk2UefiApplicationTemplate instance() {
		if(instance == null) {
			instance = new Edk2UefiApplicationTemplate();
		}
		return instance;
	}

	private Edk2UefiApplicationTemplate() {
	}
}
