package org.uefiide.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.Edk2Package;
import org.uefiide.structures.blocks.CommentBlock;
import org.uefiide.structures.blocks.Edk2ElementBlock;
import org.uefiide.structures.blocks.Edk2ElementBlockFactory;
import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;
import org.uefiide.structures.blocks.visitors.Edk2ModuleVisitor;

public class ModuleInfParser {
	private Edk2Module module;
	List<Edk2ElementBlock> rawBlocks;
	private List<String> sources = new LinkedList<String>();
	private List<String> packages = new LinkedList<String>();
	private Map<String, String> definitions = new HashMap<String, String>();
	
	private void parseModule(Edk2Module module) {
		rawBlocks = new LinkedList<Edk2ElementBlock>();
		BufferedReader fileReader = null;
		
		try {
			fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(module.getElementPath())));
			String line;
			Edk2ElementBlock currentBlock = null;
			
			while((line = fileReader.readLine()) != null) {
				line = line.trim();
				
				if(line.isEmpty()) {
					continue;
				}
				if(isComment(line)) {
					CommentBlock comment = new CommentBlock();
					comment.addLine(line);
					rawBlocks.add(comment);
					continue;
				}
				if(isSectionStart(line)) {
					if(currentBlock != null) {
						rawBlocks.add(currentBlock);
					}
					currentBlock = Edk2ElementBlockFactory.createElementBlock(line);
					continue;
				}
				
				try {
					currentBlock.addLine(line);
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
				}
				
				extractModuleInformation();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public ModuleInfParser(Edk2Module module){
		this.module = module;
		parseModule(module);
	}
	
	public String getModuleName() {
		return this.getModuleDefines().get("BASE_NAME");
	}
	
	public Map<String, String> getModuleDefines() {
		return this.definitions;
	}
	
	public List<String> getModulePackages() {
		return this.packages;
	}
	
	public List<String> getModuleSources() {
		return this.sources;
	}

	private boolean isComment(String line) {
		return line.startsWith("#");
	}
	
	private boolean isSectionStart(String line) {
		return line.startsWith("[");
	}
	
	private void extractModuleInformation() {
		Edk2ElementBlockVisitor visitor = new Edk2ModuleVisitor(sources, packages, definitions);
		for(Edk2ElementBlock block : rawBlocks) {
			block.accept(visitor);
		}
	}

	public List<Edk2ElementBlock> getRawBlocks() {
		return this.rawBlocks;
	}
}
