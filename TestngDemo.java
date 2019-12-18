
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.ITestContext;

import static org.testng.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
//import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;



public class TestngDemo {

    
        public WebDriver driver;
        public String url="https://dcuat.transunion.com.ph/dc/portal33-aqm/login.aspx";
        String testName;
        int count = 0;
       
    
	@BeforeSuite
    public void setUp()
    {   
        
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        System.out.println("The setup process is completed");
    }
    
    @BeforeTest
    public void profileSetup(final ITestContext testContext)
    {
        driver.manage().window().maximize();
        testName = testContext.getName();
        System.out.println(testName);
        System.out.println("The profile setup process is completed");
        
    }
    
    @BeforeClass
    public void appSetup()
    {
    	driver.get(url);
        System.out.println("The app setup process is completed");
    }
    
    @Test(description="This Test validates the invalid LogIn ", priority = 1, dataProvider="InvalidCredentials")
    public void  testInvalidLogin(String userName, String password)
    {
          
          driver.findElement(By.xpath("//input[@id='cpC_dcLogin_txtUserId']")).sendKeys(userName);
          driver.findElement(By.xpath("//input[@id='cpC_dcLogin_txtPassword']")).sendKeys(password);
          driver.findElement(By.xpath("//div[@class='button fs-bold text-center']")).click();
          String errormsg = driver.findElement(By.xpath("//div[@class='error-message']")).getText();
          String expectedErrorMsg = "Invalid UserId or Password.";
          WebElement userNameField = driver.findElement(By.xpath("//input[@id='cpC_dcLogin_txtUserId']"));
          userNameField.clear();
          assertEquals(errormsg, expectedErrorMsg, "ErrorMessage did not matched");
          
         
    }
    
    @Test(description="This Test validates the LogIn ", priority = 2, dataProvider="UserCredentials", alwaysRun=true)
    public void  testValidLogin(String userName, String password)
    {
          
    	  WebElement userNameField = driver.findElement(By.xpath("//input[@id='cpC_dcLogin_txtUserId']"));
          userNameField.clear();
          userNameField .sendKeys(userName);
          driver.findElement(By.xpath("//input[@id='cpC_dcLogin_txtPassword']")).sendKeys(password);
          driver.findElement(By.xpath("//div[@class='button fs-bold text-center']")).click();
          String homePageHeader = driver.findElement(By.xpath("//h1[contains(text(),'DecisionEdge Web Portal')]")).getText();
          Assert.assertTrue(homePageHeader.equalsIgnoreCase("DecisionEdge Web Portal"), "Home page header did not matched");
         
    }
    
    @Test(dependsOnMethods = "testValidLogin" , description="This Test validates the loggedin user name ")
    @Parameters("logedinUserName")
    public void testUserName(String expectedUserName ) 
    {
        String welcomeText = driver.findElement(By.xpath("//div[@class='mainWrapper']//div[3]//button[1]")).getText();
        String actualUserName = welcomeText.replace("Welcome! ", "");
		assertEquals(actualUserName, expectedUserName, "UserName did not matched");
        //System.out.println("");
    }
    
    @Test(priority= 4, description="This Test validates the mysites Header Text ")
    public void testMySitesHeader() 
    {
    	driver.findElement(By.xpath("//span[text()='My Sites']")).click();
        String actualHeaderText = driver.findElement(By.xpath("//span[@id='moduleTitle']")).getText();
        String expectedHeaderText = "My Applications";
		assertEquals(actualHeaderText, expectedHeaderText, "UserName did not matched");
		driver.findElement(By.xpath("//span[contains(text(),'Home')]")).click();
        //System.out.println("");
    }
    
    @Test(priority= 5,description="This Test validates the Default Language ")
    public void testDefaultLanguage() 
    {
    	String actualLanguage = driver.findElement(By.xpath("//span[@id='ucSecureHeader_lblSelectedLang']")).getText();
        String expectedLanguage = "English (United States)";
		assertEquals(actualLanguage, expectedLanguage, "Language did not matched");
        //System.out.println("");
    }
    
    @Test(priority= 6,description="This Test validates the Logout Text ")
    public void testLogout() 
    {
    	driver.findElement(By.xpath("//div[@class='mainWrapper']//div[3]//button[1]")).click();
    	driver.findElement(By.xpath("//a[@id='ucSecureHeader_lnkLogOut']")).click();
    	String actualLogoutText = driver.findElement(By.xpath("//div[@class='mainContainer']/div/div")).getText();
        String expectedLogoutText = "You have successfully logged out! Click here to log back in.";
		assertEquals(actualLogoutText, expectedLogoutText, "Logout message did not matched");
        //System.out.println("");
    }
    
    @Test(expectedExceptions = { IOException.class }, enabled=true)
    public void exceptionTestOne() throws Exception {
        throw new IOException();
    }
    
    @AfterMethod
    public void screenShot() throws IOException
    {
    	 TakesScreenshot ts = (TakesScreenshot)driver;
         File source = ts.getScreenshotAs(OutputType.FILE);
         count++;
         String screenShotName = Integer.toString(count);
		 String dest = System.getProperty("user.dir") +"/reports/TestCase_"+screenShotName+".png";
         File destination = new File(dest);
         FileUtils.copyFile(source, destination);
         //System.out.println("Screenshot of the test is taken");
       
    }
    
    @AfterClass
    public void closeUp()
    {
       
        System.out.println("The close_up process is completed");
    }
    
    @AfterTest
    public void reportReady()
    {
    	
        System.out.println("Report is ready to be shared, with screenshots of tests");
    }
    
    @AfterSuite
    public void cleanUp()
    {
    	driver.close();
        System.out.println("All close up activities completed");
    }
    
    @BeforeGroups("urlValidation")
    public void setUpSecurity() {
        System.out.println("url validation test starting");
    }
  
    @AfterGroups("urlValidation")
    public void tearDownSecurity() {
        System.out.println("url validation test finished");
    }
    
    @DataProvider(name="UserCredentials")
    public Object[][] getDataFromDataprovider(){
    return new Object[][] 
    	{
            { "bdoaqm_uatnudhyas", "Nov@2019" },
            
        };

    }
    
    @DataProvider(name="InvalidCredentials")
    public Object[][] getDataForInvalidLogin(){
    return new Object[][] 
    	{
            { "bdoaqm_uatnudas", "Nov@2019" },
            { "bdoaqm_uatnudhyas", "Nov2019" },
            { "bas", "Nov" }
            
            
            
        };

    }
    
    
    
}