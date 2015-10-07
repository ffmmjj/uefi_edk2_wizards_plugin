package org.uefiide.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatCodePointException;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.Edk2Package;

public class ModuleInfParser {
	private Edk2Module module;
	private BufferedReader fileReader;
	private List<Edk2Package> packages;
	private String name;
	
	
	public ModuleInfParser(Edk2Module module){
		this.module = module;
		try {
			fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(module.getElementPath())));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getModuleName() {
		if(this.name == null) {
			this.name = extractModuleName();
		}
		return this.name;
	}
	
	public List<Edk2Package> getModulePackages() {
		if(this.packages == null) {
			this.packages = extractModulePackages();
		}
		return this.packages;
	}

	private List<Edk2Package> extractModulePackages() {
		String line;
		boolean packagesSectionFound = false;
		IPath workspacePath = new Path(this.module.getWorkspacePath());
		List<Edk2Package> foundPackages = new LinkedList<Edk2Package>();
		
		try {
			while((line = fileReader.readLine()) != null) {
				if(packagesSectionFound) {
					line = line.trim();
					if(line.contains("[")) {
						break;
					}
					if(line.isEmpty()) {
						continue;
					}
					if(line.startsWith("#")) {
						continue;
					}
					
					IPath packagePath = new Path(workspacePath.append(line.trim()).toString());
					foundPackages.add(new Edk2Package(packagePath.toString(), workspacePath.toString()));
				} else if(line.contains("[Packages]")) {
					packagesSectionFound = true;
				}  
					
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return foundPackages;
	}

	private String extractModuleName() {
		String line;
		boolean definesSectionFound = false;
		
		try {
			while((line = fileReader.readLine()) != null) {
				if(line.contains("[Defines]")) {
					definesSectionFound = true;
				} else if(definesSectionFound) { 
					if(line.trim().startsWith("BASE_NAME")) {
						// TODO check if the inf is well-formed before assuming that this expression is valid
						return line.split("=")[1].trim();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
