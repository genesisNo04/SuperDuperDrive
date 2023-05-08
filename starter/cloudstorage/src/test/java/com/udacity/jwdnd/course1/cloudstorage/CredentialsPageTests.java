package com.udacity.jwdnd.course1.cloudstorage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credentials;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class CredentialsPageTests {

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "login-button")
    private WebElement loginBtn;

    @FindBy(id = "error-msg")
    private WebElement errorMsg;

    private String firstName = "genesis";
    private String lastName = "kaiser";
    private String username = "genesis";
    private String password = "kaiser";
    private String url = "http://www.google.com";

    private String usernameUpdated = "genesis123";
    private String passwordUpdated = "kaiser123";
    private String urlUpdated = "http://www.google123.com";

    @FindBy(id = "inputLastName")
    private WebElement inputLastNameRegister;

    @FindBy(id = "inputFirstName")
    private WebElement inputFirstNameRegister;

    @FindBy(id ="buttonSignUp")
    private WebElement buttonSignUp;

    @FindBy(id ="success-msg")
    private WebElement successMsg;

    @FindBy(id ="logoutButton")
    private WebElement logoutBtn;

    @FindBy(id ="nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id ="addNewCredentials")
    private WebElement addNewCredentialsBtn;

    @FindBy(id ="credential-url")
    private WebElement credentialsUrlInput;

    @FindBy(id ="credential-username")
    private WebElement credentialsUsernameInput;

    @FindBy(id ="credential-password")
    private WebElement credentialsPasswordInput;

    @FindBy(xpath = "(//button[text()='Save changes'])[2]")
    private WebElement credentialsSubmitBtn;

    @FindBy(id ="successHere")
    private WebElement successHereLnk;

    @FindBy(xpath = "(//button[text()='Close'])[2]")
    private WebElement closeCredentialsModel;

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    public CredentialsPageTests() {
        PageFactory.initElements(driver, this);
    }

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
    }

    //Login steps
    public void doLogIn(String userName, String password) {
        driver.get("http://localhost:" + port + "/login");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        webDriverWait.until(ExpectedConditions.visibilityOf(inputUsername));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOf(inputPassword));
        inputPassword.click();
        inputPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOf(loginBtn));
        loginBtn.click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    private void doMockSignUp(String firstName, String lastName, String userName, String password){
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/register");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOf(inputFirstNameRegister));
        inputFirstNameRegister.click();
        inputFirstNameRegister.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOf(inputLastNameRegister));
        inputLastNameRegister.click();
        inputLastNameRegister.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOf(inputUsername));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOf(inputUsername));
        inputPassword.click();
        inputPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOf(buttonSignUp));
        buttonSignUp.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(successMsg));
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        Assertions.assertTrue(successMsg.getText().contains("You successfully signed up!"));
    }

    @Test
    @Order(1)
    public void createNewCredentials() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        doMockSignUp(firstName, lastName, username, password);
        doLogIn(username, password);
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(addNewCredentialsBtn));
        addNewCredentialsBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsUrlInput));
        credentialsUrlInput.sendKeys(url);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsUsernameInput));
        credentialsUsernameInput.sendKeys(username);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsPasswordInput));
        credentialsPasswordInput.sendKeys(password);
        credentialsSubmitBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(successHereLnk));
        successHereLnk.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + url + "']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='" + url + "']")).isDisplayed());
        Assertions.assertNotEquals(driver.findElement(By.xpath("//th[text()='" + url + "']/following-sibling::td[2]")).getAttribute("value"), password);
    }

    @Test
    @Order(2)
    public void logoutPersitData() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        logoutBtn.click();
        doLogIn(username, password);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + url + "']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='" + url + "']")).isDisplayed());
    }

    @Test
    @Order(3)
    public void viewCredentials() throws InterruptedException {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + url + "']")));
        WebElement editButton = driver.findElement(By.xpath("//th[text()='" + url + "']//ancestor::tr//button"));
        editButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsUrlInput));
        Assertions.assertEquals(credentialsUrlInput.getAttribute("value"), url);
        Assertions.assertEquals(credentialsUsernameInput.getAttribute("value"), username);
        Assertions.assertEquals(credentialsPasswordInput.getAttribute("value"), password);
        //update
        credentialsUrlInput.clear();
        credentialsUrlInput.sendKeys(urlUpdated);
        credentialsUsernameInput.clear();
        credentialsUsernameInput.sendKeys(usernameUpdated);
        credentialsPasswordInput.clear();
        credentialsPasswordInput.sendKeys(passwordUpdated);
        credentialsSubmitBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(successHereLnk));
        successHereLnk.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));
        credentialsTab.click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + urlUpdated + "']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='" + urlUpdated + "']")).isDisplayed());
    }

    @Test
    @Order(4)
    public void deleteCredentials() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + urlUpdated + "']")));
        WebElement deleteButton = driver.findElement(By.xpath("//th[text()='" + urlUpdated + "']//ancestor::tr//a"));
        deleteButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(successHereLnk));
        successHereLnk.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialsTab));
        credentialsTab.click();
        Assertions.assertTrue(driver.findElements(By.xpath("//th[text()='" + urlUpdated + "']")).size() == 0);
    }


}
