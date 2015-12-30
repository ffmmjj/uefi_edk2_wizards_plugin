package org.uefiide.structures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.blocks.Edk2ElementBlock;
import org.uefiide.structures.blocks.visitors.BlockUpdateVisitor;
import org.uefiide.structures.parsers.ModuleInfParser;

public class Edk2Module extends Edk2Element {
	public enum Edk2ModuleType {
		UEFI_APPLICATION,
		UEFI_STDLIB_APPLICATION,
		UEFI_DRIVER,
		LIBRARY_CLASS_IMPLEMENTATION
	};
	
	private ModuleInfParser parser;
	private List<String> sources = new LinkedList<String>();
	private List<String> packages = new LinkedList<String>();
	private List<String> libraries = new LinkedList<String>();
	private Map<String, String> definitions = new HashMap<String, String>();
	
	boolean dirty = false;
	
	public Edk2Module(String path) throws FileNotFoundException, IOException {
		super(path);
		initialize();
	}

	public Edk2Module(String path, String workspacePath) throws FileNotFoundException, IOException {
		super(path, workspacePath);
		initialize();
	}
	
	public Edk2Module(String path, String workspacePath, InputStream contents) throws FileNotFoundException, IOException {
		super(path, workspacePath, contents);
		initialize();
	}
	
	public String getName() {
		String filename = new Path(this.getElementPath()).lastSegment();
		
		return filename.substring(0, filename.lastIndexOf('.'));
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
		dirty = true;
	}
	
	public void setPackages(List<String> packages) {
		this.packages = packages;
		dirty = true;
	}
	
	public void setDefinitions(Map<String, String> definitions) {
		this.definitions = definitions;
		dirty = true;
	}
	
	@Override
	public void save() {
		try {
			PrintWriter writer = new PrintWriter(this.getElementPath(), "UTF-8");
			BlockUpdateVisitor updateVisitor = new BlockUpdateVisitor(this.sources, 
												this.packages,
												this.libraries,
												this.definitions);
			
			List<Edk2ElementBlock> blocks = this.parser.getRawBlocks();
			for(Edk2ElementBlock block : blocks) {
				if(dirty) {
					block.accept(updateVisitor);
				}
				writer.println(block.toString());
			}
			writer.close();
				
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initialize() throws IOException, FileNotFoundException {
		parser = new ModuleInfParser(this);
		this.sources = parser.getModuleSources();
		this.definitions = parser.getModuleDefines();
	}
	
	public List<String> getEdk2PackageNames() {
		return this.parser.getModulePackages();
	}
	
	public List<String> getLibraryClasses() {
		return this.libraries;
	}
	
	public void setLibraryClasses(List<String> libraries) {
		this.libraries = libraries;
		dirty = true;
	}
}