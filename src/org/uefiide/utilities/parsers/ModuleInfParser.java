package org.uefiide.utilities.parsers;

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
	
	private void parseModule(Edk2Module module) throws IOException, FileNotFoundException {
		rawBlocks = new LinkedList<Edk2ElementBlock>();
		BufferedReader fileReader = null;
		
		try {
			fileReader = new BufferedReader(new InputStreamReader(module.getContentsStream()));
			String line;
			Edk2ElementBlock currentBlock = null;
			
			while((line = fileReader.readLine()) != null) {
				currentBlock = processLine(line, currentBlock);
			}
			this.rawBlocks.add(currentBlock);
			extractModuleInformation();
			
		} finally {
			fileReader.close();
		}
	}
	
	
	public ModuleInfParser(Edk2Module module) throws IOException, FileNotFoundException {
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
	
	public List<Edk2ElementBlock> getRawBlocks() {
		return this.rawBlocks;
	}

	private boolean isComment(String line) {
		return line.startsWith("#") || line.startsWith("//");
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
	
	private Edk2ElementBlock processLine(String line, Edk2ElementBlock currentBlock) {
		line = line.trim();
		
		if(line.isEmpty()) {
			return currentBlock;
		}
		if(isComment(line)) {
			CommentBlock comment = new CommentBlock();
			comment.addLine(line);
			rawBlocks.add(comment);
			return currentBlock;
		}
		// Remove trailing comments in line
		int commentMarkIndex = line.indexOf('#');
		if(commentMarkIndex != -1) {
			line = line.substring(0, commentMarkIndex);
		}
		
		if(isSectionStart(line)) {
			if(currentBlock != null) {
				rawBlocks.add(currentBlock);
			}
			currentBlock = Edk2ElementBlockFactory.createElementBlock(line);
			return currentBlock;
		}
		
		try {
			currentBlock.addLine(line);
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		return currentBlock;
	}
}
