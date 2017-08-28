package com.test.automation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class ActionMethods 
{
	public static Map<String,String> map=new HashMap<String,String>();
	
	public static String clickit(WebDriver driver,String action,String locatorName,String locatorData,ExtentTest testReport)
	{
		try 
		{
			WebDriverWait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
			driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).click();
		} 
		catch (Exception e) 
		{
			testReport.log(LogStatus.FAIL,"Click operation failed"/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			return "FAIL";
		}
		testReport.log(LogStatus.PASS,"Clicked successfully");
		return "PASS";
	}
	
	public static String sendkeys(WebDriver driver,String action,String locatorName,String locatorData,String testdata,ExtentTest testReport)
	{
		String[] data=null;
		if(testdata.length()>=4 && testdata.substring(0,4).equalsIgnoreCase("var_"))
		{
			data =testdata.split("_");
			if(data[0].equalsIgnoreCase("var"))
			{
				testdata= map.get(data[1]);
			}
		}
		try 
		{
			WebDriverWait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
			driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(testdata);
		} 
		catch (Exception e) 
		{
			testReport.log(LogStatus.FAIL,"Not able to input the text"/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			System.out.println("FAIL");
			return "FAIL";
		}
		testReport.log(LogStatus.PASS,"Able to input the text");
		return "PASS";	
	}
	
	public static String sendDynamickeys(WebDriver driver,String action,String locatorName,String locatorData,String testdata,ExtentTest testReport)
	{
		String tempData = null;
		try 
		{
			WebDriverWait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
			tempData=new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());
			if(testdata==null)
			{
				driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(tempData);
				testReport.log(LogStatus.PASS,"Able to input the text: "+tempData);
				return "PASS";
			}
			else
			{
				driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(tempData+testdata);
				testReport.log(LogStatus.PASS,"Able to input the text: "+tempData+testdata);
				return "PASS";
			}	
		} 
		catch (Exception e) 
		{
			if(testdata.equals(""))
			testReport.log(LogStatus.FAIL,"Not able to input the text: "+tempData);
			else
			testReport.log(LogStatus.FAIL,"Not able to input the text: "+tempData+testdata);	
			
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
		
			return "FAIL";
		}	
	}
	
	public static String dropdown(WebDriver driver,String action,String locatorName,String locatorData,String dropdownvalue,ExtentTest testReport)
	{
		String[] data=null;
		if(dropdownvalue.length()>=4 && dropdownvalue.substring(0,4).equalsIgnoreCase("var_"))
		{
			data =dropdownvalue.split("_");
			if(data[0].equalsIgnoreCase("var"))
			{
				dropdownvalue= map.get(data[1]);
			}
		}
		try 
		{
			WebDriverWait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));	
		    new Select(driver.findElement(new LocatorClass().getLocator(locatorName,locatorData))).selectByValue(dropdownvalue);
		} 
		catch (Exception e) 
		{
			testReport.log(LogStatus.FAIL,"Not able to select value from dropdown"/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
		
			return "FAIL";
		}
		testReport.log(LogStatus.PASS,"Successfully selected ");
		return "PASS";
	}
	
	//for lookup locator value:  xpath,id,tag
	public static String lookup(WebDriver driver,String action,String locatorName,String locatorData,String lookupValue,ExtentTest testReport)
	{
		try 
		{
			String[] str=locatorData.split(",");
	
			WebDriverWait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(new LocatorClass().getLocator(locatorName,str[0]))));	
			WebElement webElement = driver.findElement(new LocatorClass().getLocator(locatorName,str[0]));
			webElement.sendKeys(Character.toString(lookupValue.charAt(0)));
			
			WebElement autoOptions = driver.findElement(By.id(str[1]));
			wait.until(ExpectedConditions.visibilityOf(autoOptions));
			
			List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName(str[2]));
			for(WebElement option : optionsToSelect)
			{
		        if(option.getText().contains(lookupValue))
		        {
		            option.click();
		            break;
		        }
		    }
		} 
		catch (Exception e) 
		{
			testReport.log(LogStatus.FAIL,"Not able to select value from lookup"/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			return "FAIL";
		}
		testReport.log(LogStatus.PASS,"Successfully selected ");
		return "PASS";
	}
	
	public static String URL(WebDriver driver,String action,String testdata,ExtentTest testReport)
	{
		String[] data=null;
		if(testdata.length()>=4 && testdata.substring(0,4).equalsIgnoreCase("var_"))
		{
			data =testdata.split("_");
			if(data[0].equalsIgnoreCase("var"))
			{
				testdata= map.get(data[1]);
			}
		}
		try
		{
			driver.get(testdata);
		} 
		catch (Exception e) 
		{
			testReport.log(LogStatus.FAIL,"Not able to enter URL");
			testReport.log(LogStatus.FAIL,e.getMessage().toString()/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
		
			return "FAIL";
		}
		testReport.log(LogStatus.PASS,"URL entered successfully");
		return "PASS";
		
	}
	
	public static String loop(WebDriver driver,String action,String locator,String input1,String input2,Connection c,ExtentTest testReport,int usecase_id)
	{
		String[] str=input2.split("-");
		
		Statement st=null;
		ResultSet rs=null;
		
	    try
	    {
	    	for(int i=0;i<Integer.parseInt(input1);i++)
	    	{
	    		st=c.createStatement();
	    		String sqlQuery="SELECT description, action, locators, element_identifier, element_value, seq FROM qa_app_action WHERE use_case_id = "+usecase_id+" AND seq BETWEEN '"+str[0]+"' AND '"+str[1]+"' ORDER BY seq;";
	    		rs = st.executeQuery(sqlQuery);
	    		ActionClass ac=new ActionClass();
	    		while(rs.next())
	    		{
	    			String desc=rs.getString("description");
	    			String actionType=rs.getString("action");
	    			String locators=rs.getString("locators");
	    			String locatorName=rs.getString("element_identifier");
	    			String testData=rs.getString("element_value");
	    			String msg=desc+" :: "+action+" :: "+locators+" :: "+locatorName+" :: "+testData;
	    			testReport.log(LogStatus.INFO,msg);
	    			ac.callActionMethods(driver,actionType,locators,locatorName,testData,c,testReport,usecase_id);
	    		}
	    	}
	    }
	    catch (SQLException e)
	    {
	    	testReport.log(LogStatus.FAIL,"Failed to Loop"+e.getMessage()/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
	 		return "FAIL";
	 	}	
	     testReport.log(LogStatus.PASS,"Looped successfully for "+Integer.parseInt(input1)+" times from "+str[0]+" to "+str[1]+"th step");
	     	return "PASS";
	}
	
	public static String presenceOfElement(WebDriver driver,String action,String locatorName,String locatorData,String testdata,ExtentTest testReport)
	{
		String[] data = null;
		try
		{
			WebDriverWait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
			String text = driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).getText();
			if(testdata.length()>=4 && testdata.substring(0,4).equalsIgnoreCase("var_"))
			{
				data =testdata.split("_");
				if(data[0].equalsIgnoreCase("var"))
				{
					testdata= map.get(data[1]);
				}
			}

			if(text.equals(testdata))
			{
				testReport.log(LogStatus.PASS,"Element "+testdata+" is present");	
				return "PASS";
			}
			else
			{
				testReport.log(LogStatus.FAIL,"Element "+testdata+" is not present"/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);	
				return "FAIL";
			}
		}
		catch(Exception e)
		{		
			testReport.log(LogStatus.FAIL,"Element is not present or Invalid LocatorValue"+e.getMessage()/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
			return "FAIL";
		}			
	}
	
	public static String pause(WebDriver driver,String action,String testdata,ExtentTest testReport)
	{
		long number = Long.parseLong(testdata);
		try 
		{
			Thread.sleep(number);
			testReport.log(LogStatus.PASS,"Paused for "+testdata+" sec");
			return "PASS";
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			testReport.log(LogStatus.PASS,"Paused for "+testdata+" sec");
			return "FAIL";
		}
		
	}
	
	public static String getPageTitle(WebDriver driver,String action,String locatorName,String locatorData,String testData,ExtentTest testReport)
	{
		String title=driver.getTitle();
		String[] data = null;
		
		if(!testData.equals(""))
		{
			if(testData.length()>=4 && testData.substring(0,4).equalsIgnoreCase("var_"))
			{
				data =testData.split("_");
	    		if(data[0].equalsIgnoreCase("var"))
	    		{
	    			map.put(data[1], title);
	    			testReport.log(LogStatus.PASS,"The text :"+title+" has stored successfully in the variable :"+data[1]);
	    			return "PASS";
	    		}
	    		else
	    		{
	    			testReport.log(LogStatus.PASS,"The "+data+" is not recognised as a varible");
	    			return "PASS";
	    		}
	    	}
			else
			{
				if(title.equals(testData))
				{
					testReport.log(LogStatus.PASS,"Actual Page Title:"+title+" :: Expected Page Title:"+testData);
					return "PASS";
				}
				else
				{
					testReport.log(LogStatus.FAIL,"Actual Page Title:"+title+" :: Expected Page Title:"+testData);
					return "FAIL";
				}
			}
		}
		else
		{
			testReport.log(LogStatus.INFO,"Page Title:"+title);
			return "PASS";
		}							
	}
	
	public static String getPageURL(WebDriver driver,String action,String locatorName,String locatorData,String testData,ExtentTest testReport){
		String url=driver.getCurrentUrl();
		String[] data = null;
		if(!testData.equals(""))
		{
			if(testData.length()>=4 && testData.substring(0,4).equalsIgnoreCase("var_"))
			{
    			data =testData.split("_");
    			if(data[0].equalsIgnoreCase("var"))
    			{
    				map.put(data[1], url);
    				testReport.log(LogStatus.PASS,"The text :"+url+" has stored successfully in the variable :"+data[1]);
    				return  "PASS";
    			}
    			else{
    				testReport.log(LogStatus.PASS,"The "+data+" is not recognised as a varible");
    				return "PASS";
    			}
    		}
			
			else
			{
				if(url.equals(testData))
				{
					testReport.log(LogStatus.PASS,"Actual Page URL:"+url+" :: Expected Page URL:"+testData);
					return "PASS";
				}
				else
				{
					testReport.log(LogStatus.FAIL,"Actual Page URL:"+url+" :: Expected Page URL:"+testData);
					return "FAIL";
				}
			}
		}
 		else
 		{
			testReport.log(LogStatus.INFO,"Page URL:"+url);
		    return "PASS";
 		}
	}
	
    public static String navigateBack(WebDriver driver,ExtentTest testReport)
    {	
		try
		{
			driver.navigate().back();
		} 
		catch (Exception e) 
		{
			testReport.log(LogStatus.FAIL,"Not able to navigate back to original page");
			testReport.log(LogStatus.FAIL,e.getMessage().toString()/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
			return "FAIL";
		}
		testReport.log(LogStatus.PASS,"Navigated back successfully");
		return "PASS";	
    }
    
    public static String hoverOn(WebDriver driver,String locatorName,String locatorData,ExtentTest testReport)
    {
	    try 
	    {
	    	Actions act= new Actions(driver);
	    	WebElement wb=driver.findElement(new LocatorClass().getLocator(locatorName,locatorData));
	    	act.moveToElement(wb).perform();
	    } 
	    catch (Exception e) 
	    {
	    	testReport.log(LogStatus.FAIL,"Not able to perform mouseOver operation");
			testReport.log(LogStatus.FAIL,e.getMessage().toString()/*+ testReport.addScreenCapture(GetScreenShot.capture(driver,new Model().getDateTime()))*/);
			
			return "FAIL";
		}
		testReport.log(LogStatus.PASS,"Mouse over operation done successfully");
		return "PASS";
    }
    
    //only gets the text
    public static String getText(WebDriver driver,String locatorName,String locatorData,String testData,ExtentTest testReport)
    {
    	String[] data=null;
    	String value = null;
    	try
    	{
    		WebDriverWait wait=new WebDriverWait(driver,10);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
    		value=driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).getText();
    		if(!testData.equals(""))
    		{
    			if(testData.length()>=4 && testData.substring(0,4).equalsIgnoreCase("var_"))
    			{
    				data =testData.split("_");
    				if(data[0].equalsIgnoreCase("var"))
    				{
    					map.put(data[1], value);
    					testReport.log(LogStatus.PASS,"The text :"+value+" has stored successfully in the variable :"+data[1]);
    					return "PASS";
    				}
    				else
    				{
    					testReport.log(LogStatus.PASS,"The "+data+" is not recognised as a varible");
    					return "PASS";
    				}
    			}
    			else
    			{
    	    		if(value.equals(testData)){
    	    			testReport.log(LogStatus.PASS,"The text is :"+value+" equal to the expected text :"+testData);
    	    			return "PASS";
    	    		}
    	    		else
    	    		{
    	    			testReport.log(LogStatus.FAIL,"The text is :"+value+" not equal to the text value :"+testData);
    	    			return "FAIL";
    	    		}
    			}
    		}
    	}
    	
    	catch(Exception e)
    	{
    		testReport.log(LogStatus.FAIL,"Not able to get text");
    		testReport.log(LogStatus.FAIL,e.getMessage().toString());
    		return "FAIL";
    	}
    	testReport.log(LogStatus.PASS,"Text is "+value);
    	return "PASS";
    }
    
    public static String ClickonEnter(WebDriver driver,String locatorName,String locatorData,ExtentTest testReport)
    {
    	try
    	{
    		WebDriverWait wait=new WebDriverWait(driver,10);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
    		driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(Keys.ENTER);
    	}
    	catch(Exception e)
    	{
    		testReport.log(LogStatus.FAIL,"Not able to click on enter button");
    		testReport.log(LogStatus.FAIL,e.getMessage().toString());
    		return "FAIL";
    	}
    	
    	testReport.log(LogStatus.PASS,"clicked on enter button successfully");
    	return "PASS";
    }
    
    public static String ClickonTAB(WebDriver driver,String locatorName,String locatorData,ExtentTest testReport)
    {
    	try
    	{
    		WebDriverWait wait=new WebDriverWait(driver,10);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
    		driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(Keys.TAB);
    	}
    	catch(Exception e)
    	{
    		testReport.log(LogStatus.FAIL,"Not able to click on enter button");
    		testReport.log(LogStatus.FAIL,e.getMessage().toString());
    		return "FAIL";
    	}
    	
    	testReport.log(LogStatus.PASS,"clicked on enter button successfully");
    	return "PASS";
    }
    
    //used to clear text in the text field
    public static String clearText(WebDriver driver,String locatorName,String locatorData,ExtentTest testReport)
    {
    	try
    	{
    		WebDriverWait wait=new WebDriverWait(driver,10);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
    		driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).clear();
    	}
    	catch(Exception e)
    	{
    		testReport.log(LogStatus.FAIL,"Not able to clear text");
    		return "FAIL";
    	}
        testReport.log(LogStatus.PASS,"Cleared text successfully");	
        return "PASS";
    }
    
    public static String storeDataInVariable(WebDriver driver,String locatorName,String key,String value,ExtentTest testReport)
    {
    	map.put(key, value);
    	if(key.equals("") && value.equals(""))
    	{
    		testReport.log(LogStatus.FAIL,"Please specify key and value for creating a variable");
    		return "FAIL";
    	}
    	else
    	if(key.equals(""))
    	{
    		testReport.log(LogStatus.FAIL,"Please specify key for variable"); 
    		return "FAIL";
    	}
    	else if(value.equals(""))
    	{
    		testReport.log(LogStatus.FAIL,"Please specify value for variable");	
    		return "FAIL";
    	}
    	else
    	{
    		testReport.log(LogStatus.PASS,"Stored "+value+" in "+key+" variable");
    		return "PASS";
    	}
    }
    
    //variable name should be var_variablename
    public static String getValue(WebDriver driver,String locatorName,String locatorData,String testData,ExtentTest testReport)
    {
    	String value="";
    	String[] data=null;
    	
    	try
    	{
    		WebDriverWait wait=new WebDriverWait(driver,10);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
    		value=driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).getAttribute("value");
    		if(!testData.equals(""))
    		{
    			if(testData.length()>=4 && testData.substring(0,4).equalsIgnoreCase("var_"))
    			{
    				data =testData.split("_");
    				if(data[0].equalsIgnoreCase("var"))
    				{
    					map.put(data[1], value);
    					testReport.log(LogStatus.PASS,"The value :"+value+" has stored successfully in the variable :"+data[1]);
    					return "PASS";
    				}
    				else
    				{
    					testReport.log(LogStatus.PASS,"The "+data+" is not recognised as a varible");
    					return "PASS";
    				}
    			}
    			else
    			{
    				if(value.equals(testData))
    				{
    					testReport.log(LogStatus.PASS,"The value is :"+value+" equal to the expected value :"+testData);
    					return "PASS";
    				}
    				else
    				{
    					testReport.log(LogStatus.FAIL,"The value is :"+value+" not equal to the expected value :"+testData);
    					return "FAIL";
    				}
    			}
    		}
    	}

    	catch(Exception e)
    	{
    		testReport.log(LogStatus.FAIL,"Not able to get the value");
    		testReport.log(LogStatus.FAIL,e.getMessage().toString());
    		return "FAIL";
    	}
    	testReport.log(LogStatus.PASS,"The value is :"+value);
    	return "FAIL";
    }
	
    public static String getDataFromVariable(WebDriver driver,String locatorName,String key,String value,ExtentTest testReport)
    {
    	try
    	{
    		String storedValue=map.get(key);
  
    		if(key.equals(""))
    		{
    			testReport.log(LogStatus.FAIL,"Please specify key for variable"); 
    			return "FAIL";
    		}
	 
    		else 
    		if(!value.equals(""))
    		{
    			if(storedValue.equals(value))
    			{
    				testReport.log(LogStatus.PASS,"Stored value of variable "+key+" is: "+storedValue+", and expected value is: "+value);
    				return "PASS";
    			}
    			else
    			{
    				testReport.log(LogStatus.FAIL,"Stored value of variable "+key+" is: "+storedValue+", and expected value is: "+value);
    				return "FAIL";
    			}
    		}
    		else
    		{
    			testReport.log(LogStatus.INFO,"Value stored in "+key+" is :"+storedValue);
    			return "PASS";
    		}
    	}
    	
    	catch(Exception e)
    	{
    		testReport.log(LogStatus.INFO,"variable name "+key+" is not present");
    		return "PASS";
    	}
    }
}