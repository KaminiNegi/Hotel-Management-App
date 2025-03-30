package com.spring.security.selenium;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

public class TestHotelApp {

    public WebDriver driver;

    public JavascriptExecutor js;

    public WebDriverWait wait;


    @BeforeEach
    void setUp() {
    	System.setProperty("webdriver.chrome.driver", "/Users/kamininegi/Desktop/Project CI CD/Hotel-Management-App-main/src/main/resources/chromedriver");

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--remote-allow-origins=*");

        options.addArguments("--disable-web-security");

        options.addArguments("--disable-site-isolation-trials");

        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        js = (JavascriptExecutor) driver;

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:9090/index.html#");

    }


 @AfterEach

 void tearDown() {

if (driver != null) {

 driver.quit();

}

 }


    @Test
    void testValidUserLogin(){

        driver.findElement(By.id("email")).sendKeys("Testuser1@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Testuser1");
        driver.findElement(By.xpath("//*[@class='btn btn-primary w-50']")).click();
        String welcomePage = driver.findElement(By.xpath("//*[text()='Hotel Room Booking']")).getAttribute("textContent");
       Assert.assertEquals(welcomePage,"Hotel Room Booking","Test failed");
    }
   @Test
    void testInvalidLogin()
   {
       driver.findElement(By.id("email")).sendKeys(randomString()+"@gmail.com");
       driver.findElement(By.id("password")).sendKeys(randomAlphanumeric());
       driver.findElement(By.xpath("//*[@class='btn btn-primary w-50']")).click();
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
       Alert alert = wait.until(ExpectedConditions.alertIsPresent()); // Wait for alert
       String alertText = alert.getText();
       System.out.println("Alert Text: " + alertText);
       alert.accept();
       Assert.assertEquals(alertText,"Invalid credentials or server error.");
   }
   @Test
   void testAccountRegistration()
   {
       driver.findElement(By.xpath("//*[@id=\"showRegister\"]")).click();
      driver.findElement(By.xpath("//*[@id='firstname']")).sendKeys(randomString());
      driver.findElement(By.xpath("//*[@id='lastname']")).sendKeys(randomString());
      driver.findElement(By.xpath("//*[@id='regEmail']")).sendKeys(randomString()+"@gmail.com");
      driver.findElement(By.xpath("//*[@id='regPassword']")).sendKeys(randomAlphanumeric());
      driver.findElement(By.xpath("//*[@class=\"btn btn-success w-50 d-block mx-auto\"]")).click();
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
       Alert alert = wait.until(ExpectedConditions.alertIsPresent()); // Wait for alert
       String alertTextReg = alert.getText();
       System.out.println("Alert Text: " + alertTextReg);
       alert.accept();
       Assert.assertEquals(alertTextReg,"Registration successful!");

   }
    @Test
    void testAccountRegistrationExistingEmail()
    {
        driver.findElement(By.xpath("//*[@id=\"showRegister\"]")).click();
        driver.findElement(By.xpath("//*[@id='firstname']")).sendKeys(randomString());
        driver.findElement(By.xpath("//*[@id='lastname']")).sendKeys(randomString());
        driver.findElement(By.xpath("//*[@id='regEmail']")).sendKeys("Testuser1@gmail.com");
        driver.findElement(By.xpath("//*[@id='regPassword']")).sendKeys(randomAlphanumeric());
        driver.findElement(By.xpath("//*[@class=\"btn btn-success w-50 d-block mx-auto\"]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent()); // Wait for alert
        String alertTextRegFail = alert.getText();
        System.out.println("Alert Text: " + alertTextRegFail);
        alert.accept();
        Assert.assertEquals(alertTextRegFail,"Registration failed");

    }
    @Test
    void testBookRooms(){

        driver.findElement(By.id("email")).sendKeys("Testuser2@gmail.com");
        driver.findElement(By.id("password")).sendKeys("User");
        driver.findElement(By.xpath("//*[@class='btn btn-primary w-50']")).click();
      int totalRooms =  driver.findElements(By.xpath("//*[@class='col-6 col-sm-4 col-md-3 col-lg-2 d-flex justify-content-center']")).size();
        System.out.println(totalRooms);
    WebElement btnBook =  driver.findElement(By.xpath("//*[@data-id=\"4\"]"));
      Assert.assertTrue(btnBook.isDisplayed());
        System.out.println( btnBook.getText());
        if (btnBook.getText().equals("Book"))
        {
            btnBook.click();
            String afterClk = driver.findElement(By.xpath("//*[@data-id=\"4\"]")).getText();
            
        }

    }
    @Test
    void testUnableToBookDifferentUserRoom() {

        driver.findElement(By.id("email")).sendKeys("Testuser2@gmail.com");
        driver.findElement(By.id("password")).sendKeys("User");
        driver.findElement(By.xpath("//*[@class='btn btn-primary w-50']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> rooms = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//*[@class='btn btn-light book-btn']")));

        System.out.println("Number of rooms found: " + rooms.size());

        for (WebElement room : rooms) {
            String dataId = room.getAttribute("data-id"); // Extract actual data-id
            String getRoomInfo = room.getText();
            System.out.println("Checking room with data-id: " + dataId + " | Status: " + getRoomInfo);

            if (getRoomInfo.equals("Unbook")) {
                Boolean unbookBtn = room.isSelected();
                Assert.assertFalse(unbookBtn, "Unbook button should be disabled");
            }
        }

    }

    @Test
    void testAdminLogin(){

        driver.findElement(By.id("email")).sendKeys("admin@gmail.com");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath("//*[@class='btn btn-primary w-50']")).click();
        String welcomePage = driver.findElement(By.xpath("//*[text()='Hotel Room Booking']")).getAttribute("textContent");
        Assert.assertEquals(welcomePage,"Hotel Room Booking","Test failed");
    }

    @Test
    void testAdminAccess(){

        driver.findElement(By.id("email")).sendKeys("admin@gmail.com");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath("//*[@class='btn btn-primary w-50']")).click();
       int numRows= driver.findElements(By.xpath("//*[@id=\"userTable\"]//tbody//tr")).size();
        Assert.assertEquals(numRows, 10, "Table row count mismatch!");
     int numCols =  driver.findElements(By.xpath("//*[@id=\"userTable\"]//thead//th")).size();
        Assert.assertEquals(numCols, 6, "Table column count mismatch!");
      List<WebElement> l =  driver.findElements(By.xpath("//*[@id=\"userTable\"]//thead//th"));
      for (WebElement list :l)
      {
          System.out.print(list.getText()+"\t");
      }
        System.out.println();
     for (int r=1;r<=numRows;r++)
     {
         for (int c=1;c<=numCols;c++)
         {
          String tableData =   driver.findElement(By.xpath("//*[@id=\"userTable\"]//tbody//tr["+r+"]//td["+c+"]")).getText();
             System.out.print(tableData+" ");
         }
         System.out.println(" ");
     }

    }


   //to generate random data for tests
    public String randomString()
    {
        String generatedString = RandomStringUtils.randomAlphabetic(6);
        return generatedString;
    }
    public String randomAlphanumeric()
    {
        String generatedAlphanum = RandomStringUtils.randomAlphanumeric(6);
        return generatedAlphanum;
    }
}

