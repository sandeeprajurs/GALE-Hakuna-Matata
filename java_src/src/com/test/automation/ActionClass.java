package com.test.automation;

import java.sql.Connection;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;

public class ActionClass 
{
	public String callActionMethods(WebDriver driver,String actionName,String locatorName, String locatorData, String testData,Connection c,ExtentTest testReport,int usecase_id)
	{
		String status=null;
		
		switch(actionName.toUpperCase())
		{
			case "CLICK":
				status = ActionMethods.clickit(driver, actionName, locatorName, locatorData,testReport);
				break;
		  
			case "ENTERTEXT":
				status = ActionMethods.sendkeys(driver, actionName, locatorName, locatorData, testData,testReport);
				break;
				
			case "ENTERDYNAMICTEXT":
				status=ActionMethods.sendDynamickeys(driver, actionName, locatorName, locatorData, testData,testReport);
				break;
			  
			case "SELECTFROMDROPDOWN":
				status=ActionMethods.dropdown(driver, actionName, locatorName, locatorData, testData,testReport);
				break;
			  
			case "SELECTFROMLOOKUP":
				status=ActionMethods.lookup(driver, actionName, locatorName, locatorData, testData,testReport);
				break;
			  
			case "URL":
				status=ActionMethods.URL(driver, actionName, testData,testReport);
				break;
		
			case "LOOP":
				status=ActionMethods.loop(driver, actionName,locatorName,locatorData, testData,c,testReport,usecase_id);
				break;
		  
			case "PRESENCEOFELEMENT":
				status=ActionMethods.presenceOfElement(driver, actionName, locatorName, locatorData, testData, testReport);
				break;
		    
			case "PAUSE":
				status=ActionMethods.pause(driver, actionName, testData,testReport);
				break;
			  
			case "GETPAGETITLE":
				status=ActionMethods.getPageTitle(driver, actionName, locatorName, locatorData, testData, testReport);
				break;
		      
			case "GETPAGEURL":
				status=ActionMethods.getPageURL(driver, actionName, locatorName, locatorData, testData, testReport);
				break;
		  
			case "NAVIGATEBACK":
				status=ActionMethods.navigateBack(driver, testReport);
				break;
			  
			case "HOVERON":
				status=ActionMethods.hoverOn(driver, locatorName, locatorData, testReport);
				break;
			  
			case "CLEARTEXT":
				status=ActionMethods.clearText(driver, locatorName, locatorData, testReport);
				break;
		 
			case "GETTEXT":
				status=ActionMethods.getText(driver, locatorName, locatorData,testData, testReport);
				break;
		
			case "CLICKONENTER":
		  	  	status=ActionMethods.ClickonEnter(driver, locatorName, locatorData, testReport);
		  	  	break;
		  
			case "CLICKONTAB":
				status=ActionMethods.ClickonTAB(driver, locatorName, locatorData, testReport);
				break;
			  	  
			case "STOREDATAINVARIABLE":
				status=ActionMethods.storeDataInVariable(driver, locatorName, locatorData, testData, testReport);
				break;
		  
			case "GETDATAFROMVARIABLE":
				status=ActionMethods.getDataFromVariable(driver, locatorName, locatorData, testData, testReport);
				break;
			  
			case "GETVALUE":
				status=ActionMethods.getValue(driver, locatorName, locatorData, testData, testReport);
				break;
		}
		return status;
	}
	
}
