package org.uefiide.projectmanip.internals;

import java.io.FileOutputStream;
import java.io.IOException;

import org.uefiide.projectmanip.internals.templates.Edk2UefiApplicationTemplate;
import org.uefiide.structures.Edk2Module.Edk2ModuleType;

public abstract class Edk2ModuleTemplate {
	public abstract void writeModuleTemplate(FileOutputStream writer) throws IOException;
	public abstract void writeSourceTemplate(FileOutputStream writer) throws IOException;
	
	public static Edk2ModuleTemplate get(Edk2ModuleType type) {
		Edk2ModuleTemplate moduleTemplate = null;
		
		switch(type) {
		case UEFI_APPLICATION:
			moduleTemplate = Edk2UefiApplicationTemplate.instance();
			break;
		}
		
		return moduleTemplate;
	}
}
