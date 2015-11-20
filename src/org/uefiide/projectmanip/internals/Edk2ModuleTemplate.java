package org.uefiide.projectmanip.internals;

import java.io.IOException;

import org.uefiide.projectmanip.internals.templates.Edk2UefiApplicationTemplate;
import org.uefiide.structures.Edk2Module.Edk2ModuleType;

public abstract class Edk2ModuleTemplate {
	public void createModuleTemplate(String moduleLocation, String moduleName) throws IOException {
		writeSourcesTemplate(moduleLocation, moduleName);
		writeModuleTemplate(moduleLocation, moduleName);
	}
	
	public abstract void writeModuleTemplate(String moduleLocation, String moduleName) throws IOException;
	public abstract void writeSourcesTemplate(String moduleLocation, String moduleName) throws IOException;
	
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
