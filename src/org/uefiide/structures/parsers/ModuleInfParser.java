package org.uefiide.structures.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.uefiide.structures.Edk2Module;
import org.uefiide.structures.blocks.CommentBlock;
import org.uefiide.structures.blocks.DefinitionsBlock;
import org.uefiide.structures.blocks.Edk2ElementBlock;
import org.uefiide.structures.blocks.Edk2ElementBlockFactory;
import org.uefiide.structures.blocks.Edk2ElementBlockType;
import org.uefiide.structures.blocks.LibraryClassesBlock;
import org.uefiide.structures.blocks.PackagesBlock;
import org.uefiide.structures.blocks.SourcesBlock;
import org.uefiide.structures.blocks.visitors.BlockFinderVisitor;
import org.uefiide.structures.blocks.visitors.Edk2ElementBlockVisitor;
import org.uefiide.structures.blocks.visitors.Edk2ModuleVisitor;

public class ModuleInfParser {
	List<Edk2ElementBlock> rawBlocks;
	private List<String> sources = new LinkedList<String>();
	private List<String> packages = new LinkedList<String>();
	private List<String> libraries = new LinkedList<String>();
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
			if(currentBlock != null) {
				this.rawBlocks.add(currentBlock);
			}
			extractModuleInformation();
			
		} finally {
			fileReader.close();
		}
		
		checkAndCreateMandatoryBlocks();
	}
	
	
	private void checkAndCreateMandatoryBlocks() {
		BlockFinderVisitor definitionsFinder = new BlockFinderVisitor(Edk2ElementBlockType.DEFINITIONS_BLOCK);
		BlockFinderVisitor sourcesFinder = new BlockFinderVisitor(Edk2ElementBlockType.SOURCES_BLOCK);
		BlockFinderVisitor packagesFinder = new BlockFinderVisitor(Edk2ElementBlockType.PACKAGES_BLOCK);
		BlockFinderVisitor librariesFinder = new BlockFinderVisitor(Edk2ElementBlockType.LIBRARY_CLASSES_BLOCK);
		
		for(Edk2ElementBlock block : rawBlocks) {
			block.accept(definitionsFinder);
			block.accept(sourcesFinder);
			block.accept(packagesFinder);
			block.accept(librariesFinder);
		}
		
		if(!definitionsFinder.foundBlock()) {
			rawBlocks.add(new DefinitionsBlock());
		}
		if(!sourcesFinder.foundBlock()) {
			rawBlocks.add(new SourcesBlock());
		}
		if(!packagesFinder.foundBlock()) {
			rawBlocks.add(new PackagesBlock());
		}
		if(!librariesFinder.foundBlock()) {
			rawBlocks.add(new LibraryClassesBlock());
		}
	}


	public ModuleInfParser(Edk2Module module) throws IOException, FileNotFoundException {
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
		Edk2ElementBlockVisitor visitor = new Edk2ModuleVisitor(sources, packages, libraries, definitions);
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
