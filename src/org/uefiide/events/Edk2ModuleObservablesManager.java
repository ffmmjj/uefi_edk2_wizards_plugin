package org.uefiide.events;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.uefiide.structures.Edk2Module;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;


public class Edk2ModuleObservablesManager {
	public static class Edk2ModuleChangeEvent {
		private IProject project;
		private Edk2Module oldModule;
		private Edk2Module newModule;
		
		public Edk2ModuleChangeEvent(IProject project, Edk2Module oldModule, Edk2Module newModule) {
			this.project = project;
			this.newModule = newModule;
			this.oldModule = oldModule;
		}

		public IProject getProject() {
			return project;
		}

		public Edk2Module getOldModule() {
			return oldModule;
		}
		
		public Edk2Module getNewModule() {
			return newModule;
		}
	}
	
	private static boolean initialized = false;
	
	// Event streams
	private static PublishSubject<IResourceDelta> deltaObserver;
	private static Observable<Edk2ModuleChangeEvent> moduleChangesObservable;
	
	public static void init() {
		if(initialized) {
			return ;
		}
		initialized = true;
		deltaObserver = PublishSubject.create();
		moduleChangesObservable = deltaObserver
				.map(delta -> {
					IResource resource = findInfResource(delta);
					if(resource == null) {
						return null;
					}
					
					Edk2ModuleChangeEvent returnedEvent = null;
					IProject project = resource.getProject();
					try {
						String workspacePath = project.getPersistentProperty(new QualifiedName("Uefi_EDK2_Wizards", "EDK2_WORKSPACE"));
						
						Edk2Module oldModule = getOldEdk2Module(resource, workspacePath);
						Edk2Module newModule = new Edk2Module(resource.getLocation().toString(), workspacePath);
						returnedEvent = new Edk2ModuleChangeEvent(project, oldModule, newModule);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return returnedEvent;
				})
				.filter(ev -> ev != null);
	}
	private static Edk2Module getOldEdk2Module(IResource resource, String workspacePath)
			throws CoreException, FileNotFoundException, IOException {
		Edk2Module oldModule = null;
		IFile infFile = (IFile) resource;
		IFileState[] states = infFile.getHistory(null);
		
		if(states != null) {
			IFileState lastState = states[0];
			oldModule = new Edk2Module(resource.getLocation().toString(), workspacePath, lastState.getContents());
		}
		
		return oldModule;
	}
	public static void notifyResourceChanged(IResourceDelta delta) {
		deltaObserver.onNext(delta);
	}
	
	public static Observable<Edk2ModuleChangeEvent> getProjectModuleModificationObservable() {
		return moduleChangesObservable;
	}
	
	private static IResource findInfResource(IResourceDelta delta) {
		if(delta.getResource().getName().endsWith(".inf") && delta.getKind() == IResourceDelta.CHANGED) {
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
}
