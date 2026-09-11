package steps;

import utils.DateExpressionResolver;
import base.DriverFactory;
import invensoft.testdata.ContractTestData;
import invensoft.testdata.ProductDetailRow;
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
import pages.PageObjectManager;
import utils.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static utils.Constants.setTime;

public class StepsContainer {


    private LoginPage loginPage;
    private WebDriver driver;
    private WebDriverCommonLibrary webDriverCommonLibrary;
    private PageObjectManager pageObjectManager;



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
        webDriverCommonLibrary.waitForSetTime(200);
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
        webDriverCommonLibrary.scrollIntoTheView(driver, dropdown);
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

    @When("user selects target date range from {string} to {string} for row {int}  {string} field in the {string} table")
    public void userSelectsTargetDateRangeFromToForRowFieldInTheTable(String startDateStr, String endDateStr, int rowIndex, String field, String table) {
        LocalDate startDate = DateExpressionResolver.resolve(startDateStr);
        LocalDate endDate = DateExpressionResolver.resolve(endDateStr);
        PageObjectManager.getCreateCommodityContractPage().setTargetDateRange(rowIndex, startDate, endDate);
    }

    @And("verify {string} field is displaying in list view")
    public void verifyFieldIsDisplayingInListView(String arg0) {
        WebElement listelement = driver.findElement(By.xpath("//thead[@role='rowgroup']/tr//span[normalize-space()='"+arg0+"']"));
        webDriverCommonLibrary.scrollIntoTheView(driver,listelement);
        webDriverCommonLibrary.explicitWait(driver,listelement);
        webDriverCommonLibrary.highLightTheElement(driver,listelement);
    }

    @Then("the successful validation message {string} is displayed")
    public void theSuccessfulValidationMessageIsDisplayed(String arg0) {
        By successMsg = By.xpath("//*[contains(normalize-space(.),'Contract created successfully')]");
        webDriverCommonLibrary.explicitWaitVisibilityByLocator(driver,successMsg);
    }

    @Then("capture {string} {string}")
    public void capture(String arg0, String arg1) {
        String xpath = "";
        switch (arg1) {
            case "Contract No.":
                xpath = "//tbody[contains(@class,'p-datatable-tbody')]/tr/td[count(//th[contains(normalize-space(.),'"+arg1+"')]/preceding-sibling::th)+1]";
                break;
            default:
                xpath = "//div[contains(@class,'active')]//slot[contains(@class,'title') and contains(@class,'header')]//lightning-formatted-text";
        }
        webDriverCommonLibrary.waitForSetTime(setTime / 2);
        webDriverCommonLibrary.updateTheValueInDataValuePropertiesFile(arg1, driver.findElement(By.xpath(xpath)).getText());
    }

    @When("the user selects the {string} checkbox under {string}")
    public void theUserSelectsTheCheckboxUnder(String arg0, String arg1) {
    WebElement checkbox = driver.findElement(By.xpath("//div[input[@type='checkbox'] and contains(normalize-space(.),'"+arg0+"')]/input[@type='checkbox']"));
    webDriverCommonLibrary.explicitWait(driver,checkbox);
    webDriverCommonLibrary.scrollIntoTheView(driver,checkbox);
    webDriverCommonLibrary.highLightTheElement(driver,checkbox);
    checkbox.click();
    }

    @Then("verify {string}{string} prime icon button is displayed on the Commercial Commodity Contract Management dashboard")
    public void verifyPrimeIconButtonIsDisplayedOnTheCommercialCommodityContractManagementDashboard(String arg0, String arg1) {
        webDriverCommonLibrary.highLightTheElement(driver, driver.findElement(By.xpath("//span[contains(normalize-space(.),'"+arg1+"')]")));
    }

    @When("user selects {string} from the {string} literal dropdown in the {string} section")
    public void userSelectsFromTheLiteralDropdownInTheSection(String value, String field, String section) {
        CommodityContractManagementPage commodityContractManagementPage = new CommodityContractManagementPage(DriverFactory.getDriver());
        commodityContractManagementPage.selectLiteralDropdownValue(section, field, value);
    }

    @Given("user fills product details for test case {string}")
    public void userFillsProductDetailsForTestCase(String testCaseId) throws IOException {
        ContractTestData data = TestDataReader.getByTestCaseId(testCaseId);
        List<ProductDetailRow> rows = data.getProductDetails();
        CommodityContractManagementPage page = pageObjectManager.getCommodityContractManagementPage();

        final String section = "Product Details";

        for (int i = 0; i < rows.size(); i++) {
            ProductDetailRow row = rows.get(i);
            int rowIndex = i;

            System.out.println("Row " + rowIndex + " -> product = [" + row.getProduct() + "]");

            if (isBlank(row.getProduct())) continue;
            if (i > 0) {

                System.out.println("========================================");
                System.out.println("BEFORE ADDING PRODUCT DETAIL ROW");
                System.out.println("Current row index = " + rowIndex);
                System.out.println("========================================");

                page.printProductDetailRowCount();
                page.debugLastProductDetailRow();
                page.debugProductDetailAddButtons(section);
                page.debugRowValidity(rowIndex - 1);
                page.addProductDetailRow(section);
                webDriverCommonLibrary.waitForSetTime(300);
                page.debugAllProductDetailTables();

            }
            page.debugProductDetailActionButtons(1);
            page.debugLastProductDetailRow();

            // Dropdowns
            page.selectDropdownValue(section, "Product", rowIndex, row.getProduct());
            page.userEnterInTheNumberFieldInTheTable("Units", section, rowIndex, row.getUnits());
            page.selectTableType2DropdownValues(section, "Packing", rowIndex, row.getPacking());
            // UOM intentionally skipped — auto-selected when Product is chosen (per your note)
            page.userEnterInTheNumberFieldInTheTable("Qty.", section, rowIndex, row.getQty());
            page.userEnterInTheNumberFieldInTheTable("No Of Lots.", section, rowIndex, row.getNoOfLots());
            page.userEnterInTheNumberFieldInTheTable("Price", section, rowIndex, row.getPrice());

            if (isNotBlank(row.getTargetType())) {
                page.selectProductDetailValue(section, "Target Type", rowIndex, row.getTargetType());
            }
            if (isNotBlank(row.getTradeMonth())) {
                page.selectDropdownValue(section, "Trade Month", rowIndex, row.getTradeMonth());
            }

//            // Plain text fields — each written exactly once now
//            if (isNotBlank(row.getPositionMonth())) {
//                page.enterFieldValue("Position Month", section, rowIndex, row.getPositionMonth());
//            }
            if (isNotBlank(row.getQuality())) {
                page.enterFieldValue("Quality", section, rowIndex, row.getQuality());
            }

            // Date range — delegates to your existing, verified setTargetDateRange method
            if (isNotBlank(row.getTargetDateRange())) {
                fillTargetDateRangeFromJson(row.getTargetDateRange(), rowIndex);
            }

            // certificationPremium from your sample JSON isn't wired to any UI call yet —
            // add a line here once you tell me which control it maps to.
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    // Assumed to already exist in your codebase per your comment — signature
    // shown here just for reference/consistency with the rowIndex convention above.
    private void fillTargetDateRangeFromJson(String targetDateRange, int rowIndex) {
        // Split on " - " (space, hyphen, space) specifically — NOT a bare "-" —
        // because a single expression can itself contain arithmetic like
        // "today-2" or "today+4", and a naive split on any hyphen would
        // break those. \\s+ tolerates inconsistent spacing in the JSON.
        String[] parts = targetDateRange.split("\\s+-\\s+", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "targetDateRange '" + targetDateRange + "' for row " + rowIndex +
                            " is not in the expected '<start> - <end>' format (e.g. 'today - today+4')."
            );
        }

        String startExpr = parts[0].trim();
        String endExpr = parts[1].trim();

        LocalDate startDate = DateExpressionResolver.resolve(startExpr);
        LocalDate endDate = DateExpressionResolver.resolve(endExpr);

        // buildDateRangeLocator() uses a 1-based XPath sibling predicate
        // (following-sibling::div[rowIndex]), while this loop's rowIndex is
        // 0-based to match tbody-tr JS array indexing used everywhere else.
        // Convert ONLY at this call site — do not change the verified
        // locator method itself.
        int xpathRowIndex = rowIndex + 1;

        // ASSUMPTION: setTargetDateRange lives on the same page object as
        // everything else in this loop (CommodityContractManagementPage).
        // If it's actually a separate CreateCommodityContractPage class,
        // swap this to pageObjectManager.getCreateCommodityContractPage().
        pageObjectManager.getCreateCommodityContractPage().setTargetDateRange(xpathRowIndex, startDate, endDate);
    }
}
