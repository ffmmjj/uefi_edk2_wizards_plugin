package org.uefiide.startup;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.registry.FileEditorMapping;

public class SetDefaultEditorsStartup implements IStartup {

	@Override
	public void earlyStartup() {
		EditorRegistry editorReg = (EditorRegistry)PlatformUI.getWorkbench().getEditorRegistry();
		EditorDescriptor editor = (EditorDescriptor)editorReg.findEditor("org.eclipse.ui.DefaultTextEditor");
		FileEditorMapping mapping = new FileEditorMapping("inf");
	    mapping.addEditor(editor);
	    mapping.setDefaultEditor(editor);
	    
	    IFileEditorMapping[] mappings = editorReg.getFileEditorMappings();
	    FileEditorMapping[] newMappings = new FileEditorMapping[mappings.length+1];
	    for (int i = 0; i < mappings.length; i++) {
	        newMappings[i] = (FileEditorMapping) mappings[i];
	    }
	    newMappings[mappings.length] = mapping;
	    
	    Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				editorReg.setFileEditorMappings(newMappings);
			}
		});
	    
		//reg.setDefaultEditor("inf", "org.eclipse.ui.DefaultTextEditor");
	}

}
