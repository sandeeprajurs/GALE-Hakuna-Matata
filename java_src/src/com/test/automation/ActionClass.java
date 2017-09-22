package com.test.automation;

import java.sql.Connection;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;

public class ActionClass {
	
	public void callActionMethods(WebDriver driver,String actionName,String locatorName, String locatorData, String testData,Connection c,ExtentTest testReport,int usecase_id){
		
		
		switch(actionName.toUpperCase()){
		
		  case "CLICK":
		  ActionMethods.clickit(driver, actionName, locatorName, locatorData,testReport);
		  break;
		  
		  case "ENTERTEXT":
		     System.out.println("in sendkeys");
		     ActionMethods.sendkeys(driver, actionName, locatorName, locatorData, testData,testReport);
		     System.out.println("outside send keys");
		     break;
		     
		  case "ENTERDYNAMICTEXT":
		     System.out.println("in sendDynamickeys");
		     ActionMethods.sendDynamickeys(driver, actionName, locatorName, locatorData, testData,testReport);
		     System.out.println("outside sendDynamickeys");
		     break;
			  
		  case "SELECTFROMDROPDOWN":
			  ActionMethods.dropdown(driver, actionName, locatorName, locatorData, testData,testReport);
			  break;
			  
		  case "SELECTFROMLOOKUP":
			  ActionMethods.lookup(driver, actionName, locatorName, locatorData, testData,testReport);
			  break;
			  
		  case "URL":
			  ActionMethods.URL(driver, actionName, testData,testReport);
			  break;
		
		  case "LOOP":
			  ActionMethods.loop(driver, actionName,locatorName,locatorData, testData,c,testReport,usecase_id);
			  break;
		  
		  case "PRESENCEOFELEMENT":
			  ActionMethods.presenceOfElement(driver, actionName, locatorName, locatorData, testData, testReport);
			  break;
		    
		  case "PAUSE":
			  ActionMethods.pause(driver, actionName, testData,testReport);
			  break;
			  
		  case "GETPAGETITLE":
		      ActionMethods.getPageTitle(driver, actionName, locatorName, locatorData, testData, testReport);
		      break;
		      
		  case "GETPAGEURL":
			  ActionMethods.getPageURL(driver, actionName, locatorName, locatorData, testData, testReport);
		      break;
		  
		  case "NAVIGATEBACK":
			  ActionMethods.navigateBack(driver, testReport);
			  break;
			  
		  case "HOVERON":
			  ActionMethods.hoverOn(driver, locatorName, locatorData, testReport);
			  break;
			  
		  case "CLEARTEXT":
			  ActionMethods.clearText(driver, locatorName, locatorData, testReport);
			  break;
		 
		  case "GETTEXT":
			  ActionMethods.getText(driver, locatorName, locatorData,testData, testReport);
			  break;
		
		  case "CLICKONENTER":
		  	  ActionMethods.ClickonEnter(driver, locatorName, locatorData, testReport);
		  	  break;
		  
		  case "CLICKONTAB":
			  ActionMethods.ClickonTAB(driver, locatorName, locatorData, testReport);
			  break;
			  	  
		  case "STOREDATAINVARIABLE":
			  ActionMethods.storeDataInVariable(driver, locatorName, locatorData, testData, testReport);
			  break;
		  
		  case "GETDATAFROMVARIABLE":
			  ActionMethods.getDataFromVariable(driver, locatorName, locatorData, testData, testReport);
			  break;
			  
		  case "GETVALUE":
			  ActionMethods.getValue(driver, locatorName, locatorData, testData, testReport);
			  break;
		  
		  case "DRAGANDDROP":
			  ActionMethods.dragAndDrop(driver, locatorName, locatorData, testReport);
			  break;
			  

		  case "VERIFYALL":
			  ActionMethods.verifyAll(driver, actionName, locatorName, locatorData, c, testData, testReport, usecase_id);
			  break;

		  case "VERIFYCHECKBOX":
			  ActionMethods.verifyCheckBox(driver, locatorName, locatorData, testData, testReport);
			  break;

		  case "ISEMPTY":
			  ActionMethods.isEmpty(driver, locatorName, locatorData, testData, testReport);
			  break;
		

		}
		
	}
	

}
