package com.test.automation;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class GetScreenShot {
	public static String capture(WebDriver driver,String screenShotName) 
    {   
		String url = "smb://ec2-35-161-177-204.us-west-2.compute.amazonaws.com//Share//";
		String sourcepath =Model.dateVar + Model.usecase_id+"//"+screenShotName+".png";
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "gale-ciagent", "HakunaMatata");
        TakesScreenshot takeScr = (TakesScreenshot)driver;
        File source = takeScr.getScreenshotAs(OutputType.FILE);
        Path psource=Paths.get(source.getAbsolutePath());
       
		try {
			SmbFile	newFile = new SmbFile(url+sourcepath,auth);
			try (OutputStream out = newFile.getOutputStream())
			{
			    Files.copy(psource, out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
        return sourcepath;
    }

}
