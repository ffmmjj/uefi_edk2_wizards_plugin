package org.uefiide.events;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.QualifiedName;
import org.uefiide.structures.Edk2Module;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;


public class Edk2ModuleObservablesManager {
	public static class Edk2ModuleChangeEvent {
		private IProject project;
		private Edk2Module module;
		
		public Edk2ModuleChangeEvent(IProject project, Edk2Module module) {
			this.project = project;
			this.module = module;
		}

		public IProject getProject() {
			return project;
		}

		public Edk2Module getModule() {
			return module;
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
						Edk2Module module = new Edk2Module(resource.getLocation().toString(), workspacePath);
						returnedEvent = new Edk2ModuleChangeEvent(project, module);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return returnedEvent;
				})
				.filter(ev -> ev != null);
	}
	public static void notifyResourceChanged(IResourceDelta delta) {
		deltaObserver.onNext(delta);
	}
	
	public static Observable<Edk2ModuleChangeEvent> getProjectModuleModificationObservable() {
		return moduleChangesObservable;
	}
	
	private static IResource findInfResource(IResourceDelta delta) {
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
}
