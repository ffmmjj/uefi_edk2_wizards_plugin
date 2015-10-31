package org.uefiide.utilities.parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.Edk2Package;

public class PackageDecParser {
	private Edk2Package edk2Package;
	private BufferedReader fileReader;
	List<String> includePaths;
	
	public PackageDecParser(Edk2Package p) {
		this.edk2Package = p;
		
		try {
			fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(edk2Package.getElementPath())));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> getIncludePaths() {
		if(this.includePaths == null) {
			this.includePaths = extractPackageIncludePaths();
		}
		
		return this.includePaths;
	}

	private List<String> extractPackageIncludePaths() {
		String line;
		boolean includesSectionFound = false;
		IPath packageBasePath = new Path(this.edk2Package.getElementPath()).removeLastSegments(1);
		List<String> foundPaths = new LinkedList<String>();
		
		try {
			while((line = fileReader.readLine()) != null) {
				if(includesSectionFound) {
					line = line.trim();
					if(line.contains("[") && !line.contains("[Includes]") && !line.contains("Includes.X64]")) {
						includesSectionFound = false;
						continue;
					}
					if(line.isEmpty()) {
						continue;
					}
					if(line.startsWith("#")) {
						continue;
					}
					
					IPath fullIncludePath = new Path(packageBasePath.append(line.trim()).toString());
					foundPaths.add(fullIncludePath.toString());
				} else if(line.contains("[Includes]") || line.contains("Includes.X64]")) {
					includesSectionFound = true;
				}
					
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return foundPaths;
	}
}
