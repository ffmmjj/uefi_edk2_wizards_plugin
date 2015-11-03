import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IStartup;
import org.uefiide.projectmanip.Edk2ModuleProjectCreator;
import org.uefiide.structures.Edk2Module;


public class SetResourceChangeListenerStartup implements IStartup {

	@Override
	public void earlyStartup() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				final IResource projectInf = findInfResource(event.getDelta());

				if(projectInf != null) {
					final IProject project = projectInf.getProject();
					
					WorkspaceJob job=new WorkspaceJob("Updating project"){
					    @Override 
					    public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					    	try {
								Edk2Module module = new Edk2Module(projectInf.getLocation().toString());
								
								Edk2ModuleProjectCreator.updateIncludePaths(project, module);
								Edk2ModuleProjectCreator.UpdateProjectStructure(project, new Path(module.getElementPath()).removeLastSegments(1).toString());
								project.refreshLocal(IResource.DEPTH_INFINITE,monitor);
								
								return Status.OK_STATUS;
					    	} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    	
					    	return Status.CANCEL_STATUS;
					    }
					  };
					  
					  //job.setRule(project);
					  job.schedule();
				}
			}
			
			private IResource findInfResource(IResourceDelta delta) {
				if(delta.getResource().getName().endsWith(".inf")) {
					return delta.getResource();
				}
				
				for(IResourceDelta child : delta.getAffectedChildren()) {
					IResource infResource = findInfResource(child);
					if(infResource != null) {
						return infResource;
					}
				}
				
				return null;
			}
		}, IResourceChangeEvent.POST_CHANGE);

	}

}
