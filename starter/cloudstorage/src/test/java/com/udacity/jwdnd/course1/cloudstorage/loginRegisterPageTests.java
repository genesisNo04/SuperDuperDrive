package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class loginRegisterPageTests {

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

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    public loginRegisterPageTests() {
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

    @Test
    @Order(1)
    public void testFailLogin() throws InterruptedException {
        doLogIn("genesis", "kaiser");
        Assertions.assertTrue(errorMsg.isDisplayed());
    }

    @Test
    @Order(2)
    public void homePageAccessNoLogin() {
        driver.get("http://localhost:" + port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
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
    @Order(3)
    public void signUpSuccess() {
        doMockSignUp(firstName, lastName, username, password);
        doLogIn(username, password);
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
    }

    @Test
    @Order(4)
    public void signOutSuccess() {
        logoutBtn.click();
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

}
