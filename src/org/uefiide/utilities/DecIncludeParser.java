package org.uefiide.utilities;

import java.util.ArrayList;
import java.util.List;

import org.uefiide.structures.Edk2Package;

public class DecIncludeParser {
	private Edk2Package edk2Package;
	
	public DecIncludeParser(Edk2Package edk2Package) throws IllegalArgumentException {
		if(edk2Package == null) {
			throw new IllegalArgumentException("The package passed as argument to this constructor must not be null");
		}
		
		this.edk2Package = edk2Package;
	}

	public Iterable<String> extractAbsoluteIncludePaths() {
		/*
		List<String> absoluteIncludePaths = new ArrayList<>();
		Iterable<String> relativeIncludePaths = this.edk2Package.getRelativeIncludePaths();
		
		for(String includePath : relativeIncludePaths) {
			absoluteIncludePaths.add(this.edk2Package.getWorkspacePath() + includePath);
		}
		
		return absoluteIncludePaths;
		*/
		return null;
	}
}
