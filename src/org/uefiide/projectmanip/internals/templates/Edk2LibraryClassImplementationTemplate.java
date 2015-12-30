package org.uefiide.projectmanip.internals.templates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Path;
import org.uefiide.projectmanip.ModuleProjectCreationContext;
import org.uefiide.projectmanip.internals.Edk2ModuleTemplate;
import org.uefiide.structures.Edk2Module;

public class Edk2LibraryClassImplementationTemplate extends Edk2ModuleTemplate {

	public Edk2LibraryClassImplementationTemplate(ModuleProjectCreationContext context) {
		super(context);
	}

	@Override
	public void writeModuleTemplate() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String moduleName = context.getModuleName();
		createInfFile(moduleLocation, moduleName);
		
		File libraryClassHeaderFile = new Path(context.getLibraryClassHeaderLocation()).toFile();
		if(!libraryClassHeaderFile.exists()) {
			createLibraryClassHeaderFile(libraryClassHeaderFile);
		}
	}

	private void createInfFile(String moduleLocation, String moduleName) throws FileNotFoundException, IOException {
		File infFile = new File(new Path(moduleLocation).append(moduleName + ".inf").toString());
		
		if(!infFile.exists()) {
			infFile.createNewFile();
		}
		
		Edk2Module newModule = new Edk2Module(infFile.getAbsolutePath(), context.getWorkspaceLocation());
	    Map<String, String> defines = newModule.getDefines();
	    List<String> sources = newModule.getSources();
	    List<String> libraries = newModule.getLibraryClasses();
	    List<String> packages = newModule.getEdk2PackageNames();
	    
		defines.put("MODULE_TYPE", "BASE");
		defines.put("INF_VERSION", "0x00010006");
		defines.put("BASE_NAME", moduleName);
		defines.put("VERSION_STRING", "0.1");
		defines.put("FILE_GUID", UUID.randomUUID().toString());
		defines.put("LIBRARY_CLASS", context.getLibraryClassName());
		
		sources.add(moduleName + ".c");
		
		packages.add("MdePkg/MdePkg.dec");
		
		libraries.add("UefiLib");
		
		newModule.setDefinitions(defines);
		newModule.setPackages(packages);
		newModule.setSources(sources);
		newModule.setLibraryClasses(libraries);
		newModule.save();
	}
	
	private void createLibraryClassHeaderFile(File headerFile) throws FileNotFoundException, IOException {
		if(headerFile.exists()) {
			return ;
		}
		headerFile.createNewFile();
		
		FileOutputStream writer = new FileOutputStream(headerFile);
		
		String headerName = headerFile.getName().toUpperCase();
		headerName = headerName.substring(0, headerName.lastIndexOf('.'));
		String headerGuard = "__EFI_" + headerName + "_H__";
		
		writer.write(("#ifndef " + headerGuard + "\n").getBytes());
		writer.write(("#define " + headerGuard + " \n").getBytes());
		writer.write("#include <Uefi.h>\n\n".getBytes());
		writer.write("/***** DECLARE YOUR LIBRARY CLASS FUNCTIONS HERE *****/\n\n".getBytes());
		writer.write("#endif".getBytes());
		
		writer.close();
	}

	@Override
	public void writeSourcesTemplate() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String moduleName = context.getModuleName();
		String headerFileName = new Path(context.getLibraryClassHeaderLocation()).lastSegment();
		File appSource = new File(new Path(moduleLocation).append(moduleName + ".c").toString());
		FileOutputStream writer = new FileOutputStream(appSource);
		
		if(!appSource.exists()) {
			appSource.createNewFile();
		}
		
		writer.write("#include <Uefi.h>\n".getBytes());
		writer.write(("#include <Library/" + headerFileName + ">\n").getBytes());
		writer.write("\n".getBytes());
		writer.write("/***** IMPLEMENT THE LIBRARY CLASS FUNCTIONS HERE *****/\n\n".getBytes());
		writer.write("\n".getBytes());
		
		writer.close();
	}

}
