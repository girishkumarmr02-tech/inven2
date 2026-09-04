package steps;

import base.DriverFactory;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.CommodityContractManagementPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.WebDriverCommonLibrary;

import java.time.LocalDate;

public class StepsContainer {


    private LoginPage loginPage;
    private WebDriver driver;
    private WebDriverCommonLibrary webDriverCommonLibrary;


    @Given("open browser")
    public void openBrowser() {
        driver = DriverFactory.initializeDriver(ConfigReader.getProperty("browser"));
        String url = ConfigReader.getProperty("url");
        webDriverCommonLibrary = new WebDriverCommonLibrary(driver);
        System.out.println("================================");
        System.out.println("Launching application");
        System.out.println("Browser: " + ConfigReader.getProperty("browser"));
        System.out.println("URL: " + url);
        System.out.println("================================");
        driver.get(url);
        driver.manage().window().maximize();
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page Title: " + driver.getTitle());
        loginPage = new LoginPage(driver);
    }

    @When("enter the user name as {string}")
    public void enterTheUserNameAs(String arg0) {
        String username = ConfigReader.getProperty(arg0);
        loginPage.enterUsername(username);
    }

    @When("enter the user password as {string}")
    public void enterTheUserPasswordAs(String arg0) {
        String password = ConfigReader.getProperty(arg0);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        System.out.println("After Login URL: " + driver.getCurrentUrl());
        System.out.println("After Login Title: " + driver.getTitle());

    }

    @Then("the user should be navigated to the dashboard")
    public void theUserShouldBeNavigatedToTheDashboard() {
        boolean isDashboardVisible = loginPage.isDashboardDisplayed();
        Assert.assertTrue(isDashboardVisible, "Login did not land on the dashboard as expected");
    }

    @And("verify {string} section is displayed on the dashboard page")
    public void verifyFieldIsDisplayingInLightningPage(String arg0) {
        webDriverCommonLibrary.highLightTheElement(driver, driver.findElement(By.xpath("//div[contains(@class,'content') and contains(@class,'sidenav-content')]//*[contains(text(),'" + arg0 + "')]")));
    }

    @Then("verify {string} is displayed in the dashboard sidebar menu")
    public void verifyIsDisplayedInTheDashboardSidebarMenu(String arg0) {
        webDriverCommonLibrary.highLightTheElement(driver, driver.findElement(By.xpath("//li[@routerlinkactive='active' and contains(@id,'" + arg0 + "')]")));
    }

    @When("user navigates to {string} dashboard sidebar menu")
    public void userNavigatesToDashboardSidebarMenu(String arg0) {
        WebElement menuelement = driver.findElement(By.xpath("//li[@routerlinkactive='active' and contains(@id,'" + arg0 + "')]"));
        menuelement.click();
    }

    @When("user clicks on {string} section on the dashboard page")
    public void userClicksOnSectionOnTheDashboardPage(String arg0) {
        WebElement element = driver.findElement(By.xpath("//div[contains(@class,'content') and contains(@class,'sidenav-content')]//*[contains(text(),'" + arg0 + "')]"));
        element.click();
    }

    @Then("verify {string}{string} button is displayed on the Commercial Commodity Contract Management dashboard")
    public void verifyButtonIsDisplayedOnTheCommercialCommodityContractManagementDashboard(String arg0, String arg1) {
        webDriverCommonLibrary.highLightTheElement(driver, driver.findElement(By.xpath("//p-button[contains(@icon,'pi pi-" + arg1 + "')]//button")));

    }

    @Then("verify {string}{string} prime icons is displayed on the Commercial Commodity Contract Management dashboard")
    public void verifyPrimeIconsIsDisplayedOnTheCommercialCommodityContractManagementDashboard(String arg0, String arg1) {
        webDriverCommonLibrary.highLightTheElement(driver, driver.findElement(By.xpath("//p-button[contains(@icon,'pi pi-" + arg1 + "')]//button")));

    }

    @Then("verify {string}{string} boot strap icon is displayed on the Commercial Commodity Contract Management dashboard")
    public void verifyBootStrapIconIsDisplayedOnTheCommercialCommodityContractManagementDashboard(String arg0, String arg1) {
        webDriverCommonLibrary.highLightTheElement(driver, driver.findElement(By.xpath("//p-button[contains(@icon,'" + arg1 + "')]//button")));

    }

    @When("clicks on {string} {string} icon on the Commodity Contract Management dashboard")
    public void clicksOnIconOnTheCommodityContractManagementDashboard(String arg0, String arg1) {
        WebElement element = driver.findElement(By.xpath("//p-button[contains(@icon,'pi pi-" + arg1 + "')]//button"));
        element.click();
    }

    @Then("verifies that the {string} popup is displayed")
    public void verifiesThatThePopupIsDisplayed(String arg0) {
        CommodityContractManagementPage commodityContractManagementPage = new CommodityContractManagementPage(DriverFactory.getDriver());
        commodityContractManagementPage.getCreateGeneralPopupTitle();
    }

    @Then("verifies that the {string} section is displayed")
    public void verifiesThatTheSectionIsDisplayed(String arg0) {
        WebElement element = driver.findElement(By.xpath("//span[contains(normalize-space(),'" + arg0 + "')]"));
        webDriverCommonLibrary.highLightTheElement(driver, element);
    }

    @When("user selects {string} from the {string} dropdown in the {string} section")
    public void userSelectsFromTheDropdownInTheSection(String value, String field, String section) {
        CommodityContractManagementPage commodityContractManagementPage = new CommodityContractManagementPage(DriverFactory.getDriver());
        commodityContractManagementPage.selectDropdownValue(section, field, value);
    }

    @And("click on {string} button")
    public void clickOnButton(String arg0) {
        WebElement saveButton = driver.findElement(By.xpath("//span[text()='Save']"));
        webDriverCommonLibrary.explicitWait(driver, saveButton);
        saveButton.click();
    }

    @When("user enters {string} in {string} text field in the {string} section")
    public void userEntersInTheTextFieldInTheSection(String arg0, String field, String section) {

        WebElement textField = driver.findElement(By.xpath("//span[text()='" + section + "']/following::span[@class='p-float-label'][.//span[text()='" + field + "']]//input"));
        webDriverCommonLibrary.explicitWait(driver, textField);
        webDriverCommonLibrary.scrollIntoTheView(driver, textField);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver, textField);
        textField.sendKeys(arg0);
    }

    @And("verifies that the {string} table section is displayed")
    public void verifiesThatTheTableSectionIsDisplayed(String label) {
        WebElement element = driver.findElement(By.xpath("//h3[normalize-space()='" + label + "']"));
        webDriverCommonLibrary.highLightTheElement(driver, element);
    }


    @And("verifies that the {string} column is displayed in the {string} section table")
    public void verifiesThatTheColumnIsDisplayedInTheSectionTable(String column, String section) {
        WebElement element = driver.findElement(By.xpath("//h3[normalize-space()='" + section + "']/following::th[.//span[normalize-space()='" + column + "']]"));
        webDriverCommonLibrary.highLightTheElement(driver, element);
    }

    @When("user selects {string} from the {string} dropdown in the {string} table")
    public void userSelectsFromTheDropdownInTheTable(String value, String field, String section) {
        CommodityContractManagementPage commodityContractManagementPage = new CommodityContractManagementPage(DriverFactory.getDriver());
        commodityContractManagementPage.selectTableDropdownValue(value,field,section);
    }

    @When("user select {string} from the {string} second type dropdown in the {string} table")
    public void userSelectFromTheDropdownInTheTable(String value, String field, String section) {

        // 1. Locate dropdown
        By dropdownLocator = By.xpath(
                "//h3[normalize-space()='" + section + "']" +
                        "/following::table[1]" +
                        "//tbody/tr[1]/td[" +
                        "count(" +
                        "//h3[normalize-space()='" + section + "']" +
                        "/following::table[1]" +
                        "//thead/tr/th[" +
                        ".//span[normalize-space()='" + field + "']" +
                        "]/preceding-sibling::th" +
                        ") + 1" +
                        "]" +
                        "//p-dropdown"
        );

        // 2. Wait for dropdown
        WebElement dropdown = webDriverCommonLibrary.explicitWaitByLocator(driver, dropdownLocator);

        // 3. Scroll
        webDriverCommonLibrary.scrollIntoTheView(
                driver,
                dropdown
        );

        // 4. Click
        dropdown.click();


        // 5. Locate opened PrimeNG combobox
//        By searchOptionLocator = By.xpath("//span[@role='combobox'" + " and @aria-haspopup='listbox'" + " and @aria-expanded='true']"
        By searchOptionLocator = By.xpath("//input[@role='searchbox' and @type='text']");

        WebElement selectSearchOption =
                webDriverCommonLibrary.explicitWaitVisibilityByLocator(
                        driver,
                        searchOptionLocator
                );

        // 6. Enter value
        selectSearchOption.sendKeys(value);


        // 7. Locate required option
        By optionLocator = By.xpath(
                "//ul[@role='listbox']" +
                        "//li[@role='option'" +
                        " and normalize-space(@aria-label)='" + value + "']"
        );

        // 8. Wait for option
        WebElement selectOption =
                webDriverCommonLibrary.explicitWaitByLocator(
                        driver,
                        optionLocator
                );

        // 9. Scroll
        webDriverCommonLibrary.scrollIntoTheView(
                driver,
                selectOption
        );

        // 10. Click option
        selectOption.click();
    }

    @When("user selects {string} from the {string} dropdown type in the {string} table")
    public void userSelectsFromTheDropdownTypeInTheTable(String value, String field , String section) {
        CommodityContractManagementPage commodityContractManagementPage = new CommodityContractManagementPage(DriverFactory.getDriver());
        commodityContractManagementPage.selectTableType2DropdownValue(value,field,section);
    }

    @When("user enters {string} in the {string} number field in the {string} table")
    public void userEntersInTheNumberFieldInTheTable(String value, String field, String section) {
        WebElement numberField = driver.findElement(By.xpath("//h3[normalize-space()='"+section+"']/following::table[1]//tbody/tr[1]/td[count(//h3[normalize-space()='"+section+"']/following::table[1]//thead/tr/th[.//span[normalize-space()='"+field+"']]/preceding-sibling::th)+1]//.//input"));
        webDriverCommonLibrary.explicitWait(driver,numberField);
        webDriverCommonLibrary.scrollIntoTheView(driver,numberField);
        numberField.sendKeys(value);
    }

    @When("user selects target date range from {string} to {string} in the {string} field in the {string} table")
    public void userSelectsTargetDateRangeInTheFieldInTheTable(String startDateStr, String endDateStr, String field, String table) {
        CommodityContractManagementPage commodityContractManagementPage = new CommodityContractManagementPage(DriverFactory.getDriver());
        LocalDate startDate = LocalDate.parse(startDateStr); // ISO format yyyy-MM-dd
        LocalDate endDate = LocalDate.parse(endDateStr);
        webDriverCommonLibrary.selectTargetDateRangeInTable(field, table, startDate, endDate);
    }

     @When("user enters {string} in {string} long text area field in the {string} section")
    public void userEntersInLongTextAreaFieldInTheSection(String value, String field, String section) {
        WebElement longTextArea = driver.findElement(By.xpath("//span[text()='"+section+"']/following::span[@class='p-float-label'][.//span[text()='"+field+"']]/textarea"));
        System.out.println("DEBUG -> field='" + field + "', section='" + section + "'");
        webDriverCommonLibrary.explicitWait(driver,longTextArea);
        webDriverCommonLibrary.scrollIntoTheView(driver,longTextArea);
        webDriverCommonLibrary.explicitWaitVisibility(driver,longTextArea);
        longTextArea.click();
        longTextArea.sendKeys(value);

    }

    @When("test selects target date range from {string} to {string} in the {string} field in the {string} table")
    public void testSelectsTargetDateRangeFromToInTheFieldInTheTable(String startDateStr, String endDateStr, String field, String table) {
        LocalDate startDate = LocalDate.parse(startDateStr); // ISO format yyyy-MM-dd
        LocalDate endDate = LocalDate.parse(endDateStr);
        webDriverCommonLibrary.testselectTargetDateRangeInTable(field, table, startDate, endDate);
    }
}