package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
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
public class NotesPageTests {

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "login-button")
    private WebElement loginBtn;

    @FindBy(id = "error-msg")
    private WebElement errorMsg;

    @FindBy(id = "inputLastName")
    private WebElement inputLastNameRegister;

    @FindBy(id = "inputFirstName")
    private WebElement inputFirstNameRegister;

    @FindBy(id ="buttonSignUp")
    private WebElement buttonSignUp;

    @FindBy(id ="success-msg")
    private WebElement successMsg;

    @FindBy(id ="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id ="addNewNotes")
    private WebElement addNewNotesBtn;

    @FindBy(id ="note-title")
    private WebElement noteTitleInput;

    @FindBy(id ="note-description")
    private WebElement noteDescriptionInput;

    @FindBy(xpath = "(//button[text()='Save changes'])[1]")
    private WebElement notesSubmitBtn;

    @FindBy(id ="successHere")
    private WebElement successHereLnk;

    @FindBy(xpath = "(//button[text()='Close'])[1]")
    private WebElement closeCredentialsModel;

    @FindBy(id ="logoutButton")
    private WebElement logoutBtn;

    private String firstName = "genesis";
    private String lastName = "kaiser";
    private String username = "genesis";
    private String password = "kaiser";

    private String noteTitle = "Note Title";
    private String noteDescription = "Note Description";
    private String noteTitleUpdated = "Note Title 123";
    private String noteDescriptionUpdated = "Note Description 123";

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    public NotesPageTests() {
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
    public void createNewNotes() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        doMockSignUp(firstName, lastName, username, password);
        doLogIn(username, password);
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));
        notesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(addNewNotesBtn));
        addNewNotesBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(noteTitleInput));
        noteTitleInput.sendKeys(noteTitle);

        webDriverWait.until(ExpectedConditions.visibilityOf(noteDescriptionInput));
        noteDescriptionInput.sendKeys(noteDescription);

        notesSubmitBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(successHereLnk));
        successHereLnk.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + noteTitle + "']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='" + noteTitle + "']")).isDisplayed());
    }

    @Test
    @Order(2)
    public void logoutPersitData() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        logoutBtn.click();
        doLogIn(username, password);
        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + noteTitle + "']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='" + noteTitle + "']")).isDisplayed());
    }

    @Test
    @Order(3)
    public void viewNotes() throws InterruptedException {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + noteTitle + "']")));
        WebElement editButton = driver.findElement(By.xpath("//th[text()='" + noteTitle + "']//ancestor::tr//button"));
        editButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(noteTitleInput));
        Assertions.assertEquals(noteTitleInput.getAttribute("value"), noteTitle);
        Assertions.assertEquals(noteDescriptionInput.getAttribute("value"), noteDescription);
        //update
        noteTitleInput.clear();
        noteTitleInput.sendKeys(noteTitleUpdated);
        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(noteDescriptionUpdated);
        notesSubmitBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(successHereLnk));
        successHereLnk.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));
        notesTab.click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + noteTitleUpdated + "']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='" + noteTitleUpdated + "']")).isDisplayed());
    }

    @Test
    @Order(4)
    public void deleteNotes() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='" + noteTitleUpdated + "']")));
        WebElement deleteButton = driver.findElement(By.xpath("//th[text()='" + noteTitleUpdated + "']//ancestor::tr//a"));
        deleteButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(successHereLnk));
        successHereLnk.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(notesTab));
        notesTab.click();
        Assertions.assertTrue(driver.findElements(By.xpath("//th[text()='" + noteTitleUpdated + "']")).size() == 0);
    }

}
