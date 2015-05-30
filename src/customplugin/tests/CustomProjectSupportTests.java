package customplugin.tests;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import customplugin.CustomProjectSupport;

public class CustomProjectSupportTests {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Test
	public void test() {
		try {
			File pkgfolder = folder.newFolder("AppPkg");
			File helloInf = folder.newFile("AppPkg/Hello.inf");
			folder.newFolder("AppPkg/Applications");
			File mainInf = folder.newFile("AppPkg/Applications/Main.inf");
			List<String> infs = CustomProjectSupport.findEdk2Modules(pkgfolder);
			
			Assert.assertTrue(infs.contains(helloInf.getAbsolutePath()));
			Assert.assertTrue(infs.contains(mainInf.getAbsolutePath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
