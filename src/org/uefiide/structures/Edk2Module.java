package org.uefiide.structures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.blocks.Edk2ElementBlock;
import org.uefiide.utilities.ModuleInfParser;

public class Edk2Module extends Edk2Element {
	private ModuleInfParser parser;
	private List<String> sources = new LinkedList<String>();
	private List<Edk2Package> packages = new LinkedList<Edk2Package>();
	private Map<String, String> definitions = new HashMap<String, String>();
	
	public Edk2Module(String path) throws FileNotFoundException, IOException {
		super(path);
		initialize();
	}

	public Edk2Module(String path, String workspacePath) throws FileNotFoundException, IOException {
		super(path, workspacePath);
		initialize();
	}

	public String getName() {
		return this.parser.getModuleName();
	}
	
	public Map<String, String> getDefines() {
		return this.definitions;
	}
	
	public List<Edk2Package> getPackages() {
		List<Edk2Package> packages = new LinkedList<Edk2Package>();
		IPath workspacePath = new Path(this.getWorkspacePath());
		
		for(String packageName : this.parser.getModulePackages()) {
			packages.add(new Edk2Package(workspacePath.append(packageName).toString(), this.getWorkspacePath()));
		}
		
		return packages;
	}
	
	public List<String> getSources() {
		return this.sources;
	}
	
	public void setSources(List<String> sources) {
		this.sources = sources;
	}
	
	public void setPackages(List<Edk2Package> packages) {
		this.packages = packages;
	}
	
	public void setDefinitions(Map<String, String> definitions) {
		this.definitions = definitions;
	}
	
	@Override
	public void save() {
		// TODO open a File to this module
		List<Edk2ElementBlock> blocks = this.parser.getRawBlocks();
		
		for(Edk2ElementBlock block : blocks) {
			// TODO write each block's toString() returned value into the module file
		}
	}
	
	private void initialize() throws IOException, FileNotFoundException {
		parser = new ModuleInfParser(this);
		this.sources = parser.getModuleSources();
		this.definitions = parser.getModuleDefines();
		
		IPath workspacePath = new Path(this.getWorkspacePath());
		for(String packageName : this.parser.getModulePackages()) {
			this.packages.add(new Edk2Package(workspacePath.append(packageName).toString(), this.getWorkspacePath()));
		}
	}
}