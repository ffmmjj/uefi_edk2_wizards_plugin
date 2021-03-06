package org.uefiide.projectmanip.internals;

import java.io.IOException;

import org.uefiide.projectmanip.ModuleProjectCreationContext;
import org.uefiide.projectmanip.internals.templates.Edk2LibraryClassImplementationTemplate;
import org.uefiide.projectmanip.internals.templates.Edk2UefiApplicationTemplate;
import org.uefiide.projectmanip.internals.templates.Edk2UefiDriverTemplate;
import org.uefiide.structures.Edk2Module.Edk2ModuleType;

public abstract class Edk2ModuleTemplate {
	protected ModuleProjectCreationContext context;
	
	protected Edk2ModuleTemplate(ModuleProjectCreationContext context) {
		this.context = context;
	}
	
	public void createModuleTemplate() throws IOException {
		writeSourcesTemplate();
		writeModuleTemplate();
	}
	
	protected abstract void writeModuleTemplate() throws IOException;
	protected abstract void writeSourcesTemplate() throws IOException;
	
	public static Edk2ModuleTemplate getTemplate(Edk2ModuleType type, ModuleProjectCreationContext context) {
		Edk2ModuleTemplate moduleTemplate = null;
		
		switch(type) {
		case UEFI_APPLICATION:
			moduleTemplate = new Edk2UefiApplicationTemplate(context);
			break;
			
		case LIBRARY_CLASS_IMPLEMENTATION:
			moduleTemplate = new Edk2LibraryClassImplementationTemplate(context);
			break;
			
		case UEFI_DRIVER:
			moduleTemplate = new Edk2UefiDriverTemplate(context);
			break;
			
		case UEFI_STDLIB_APPLICATION:
		default:
			break;
		}
		
		return moduleTemplate;
	}
}
