package customplugin;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

public class CustomProjectSupport {
	public static IProject createEdk2Project(String projectName, URI workspaceLocation) {
		return null;
		
	}
	
	public static List<String> findEdk2Modules(File edk2RootDir) {
		return searchInfFile(edk2RootDir);
	}
	
	private static List<String> searchInfFile(File dir) {
		List<String> infFilesHere = new ArrayList<String>();
		
		if(dir.canRead()) {
			for(File file : dir.listFiles()) {
				if(file.isDirectory()) {
					infFilesHere.addAll(searchInfFile(file));
				} else if(file.getName().endsWith(".inf")) {
					infFilesHere.add(file.getAbsolutePath());
				}
			}
		}
		
		return infFilesHere;
	}
}
