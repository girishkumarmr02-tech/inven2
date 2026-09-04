package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.WebDriverCommonLibrary;

public class CommodityContractManagementPage {

    WebDriver driver;
    WebDriverCommonLibrary webDriverCommonLibrary;

    @FindBy(xpath = "//span[contains(text(),'Commodity Contract Management - Create')]")
    private WebElement label_CreateGeneralPopupTitle;

    @FindBy(xpath = "//span[@role='combobox' and @aria-label='Class']")
    private WebElement dropdown_Class;

    public CommodityContractManagementPage(WebDriver initDriver) {
        this.driver = initDriver;
        this.webDriverCommonLibrary = new WebDriverCommonLibrary(driver);
        PageFactory.initElements(driver, this);
    }

    public void getCreateGeneralPopupTitle() {
        webDriverCommonLibrary.highLightTheElement(driver, label_CreateGeneralPopupTitle);
    }

    public void selectClassDropdown(String filterText, String optionText) {
        webDriverCommonLibrary.explicitWait(driver, dropdown_Class);
        dropdown_Class.click();

        By filterInputLocator = By.xpath("//div[contains(@class,'p-dropdown-panel')]//input[contains(@class,'p-dropdown-filter')]");
        WebElement filterInput = driver.findElement(filterInputLocator);
        webDriverCommonLibrary.explicitWait(driver, filterInput);
        filterInput.sendKeys(filterText);

        String optionXpath = "//div[contains(@class,'p-dropdown-panel')]//li[contains(@class,'p-dropdown-item') and normalize-space(text())='" + optionText + "']";
        WebElement option = driver.findElement(By.xpath(optionXpath));
        webDriverCommonLibrary.explicitWait(driver, option);
        option.click();
    }

    public void selectDropdownValue(String section, String field, String value) {

        //Find class element
        WebElement dropdown = driver.findElement(By.xpath("//p-card[@class='p-element'][.//span[contains(text(),'"+section+"')]]//span[contains(@aria-label,'"+field+"')]"));
        dropdown.click();

        //search required options
        WebElement searchboxoption = driver.findElement(By.xpath("//span[@aria-label='"+field+"' and @aria-expanded='true']/following::input[@role='searchbox']"));
        webDriverCommonLibrary.explicitWait(driver,searchboxoption);
        searchboxoption.clear();
        searchboxoption.sendKeys(value);

        //Click on required option
        WebElement selectOption = driver.findElement(By.xpath("//ul[contains(@class,'p-dropdown-items')]//span[normalize-space(text())='"+value+"']"));
        webDriverCommonLibrary.explicitWait(driver,selectOption);
        webDriverCommonLibrary.scrollIntoTheView(driver,selectOption);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver,selectOption);
    }

    public void selectTableDropdownValue(String value, String field, String section) {

        //Find field element
//        WebElement dropdown = driver.findElement(By.xpath("//h3[normalize-space()='"+section+"']/following::table[1]//tbody/tr[1]/td[count(//h3[normalize-space()='"+section+"']/following::table[1]//thead//th[.//span[normalize-space()='"+field+"']]/preceding-sibling::th)+1]//span[@role='combobox']"));
        WebElement dropdown = driver.findElement(By.xpath("//h3[normalize-space()='"+section+"']/following::table[1]//tbody/tr[1]/td[count(//h3[normalize-space()='"+section+"']/following::table[1]//thead/tr/th[.//span[normalize-space()='"+field+"']]/preceding-sibling::th)+1]//div[contains(@class,'p-dropdown-trigger')]"));
        webDriverCommonLibrary.explicitWait(driver,dropdown);
        webDriverCommonLibrary.scrollIntoTheView(driver,dropdown);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver,dropdown);

        //search required options
        WebElement selectSearchOption = driver.findElement(By.xpath("//span[@role='combobox' and @aria-haspopup='listbox' and @aria-expanded='true']"));
        webDriverCommonLibrary.scrollIntoTheView(driver,selectSearchOption);
        webDriverCommonLibrary.explicitWait(driver,selectSearchOption);
        webDriverCommonLibrary.explicitWaitVisibility(driver,selectSearchOption);
        selectSearchOption.sendKeys(value);

        //Click on required option
        WebElement selectOption = driver.findElement(By.xpath("//ul[contains(@role,'listbox')]//li[@role='option' and normalize-space(@aria-label)='"+value+"']"));
        webDriverCommonLibrary.explicitWait(driver,selectOption);
        webDriverCommonLibrary.scrollIntoTheView(driver,selectOption);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver,selectOption);
    }

    public void selectTableType2DropdownValue(String value, String field, String section) {

        //Find field element
//        WebElement dropdown = driver.findElement(By.xpath("//h3[normalize-space()='"+section+"']/following::table[1]//tbody/tr[1]/td[count(//h3[normalize-space()='"+section+"']/following::table[1]//thead//th[.//span[normalize-space()='"+field+"']]/preceding-sibling::th)+1]//span[@role='combobox']"));
        WebElement dropdown = driver.findElement(By.xpath("//h3[normalize-space()='"+section+"']/following::table[1]//tbody/tr[1]/td[count(//h3[normalize-space()='"+section+"']/following::table[1]//thead/tr/th[.//span[normalize-space()='"+field+"']]/preceding-sibling::th)+1]//div[contains(@class,'p-dropdown-trigger')]"));
        webDriverCommonLibrary.explicitWait(driver,dropdown);
        webDriverCommonLibrary.scrollIntoTheView(driver,dropdown);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver,dropdown);

        //search required options
        WebElement selectSearchOption = driver.findElement(By.xpath("//input[@role='searchbox' and @type='text']"));
        webDriverCommonLibrary.scrollIntoTheView(driver,selectSearchOption);
        webDriverCommonLibrary.explicitWait(driver,selectSearchOption);
        webDriverCommonLibrary.explicitWaitVisibility(driver,selectSearchOption);
        selectSearchOption.sendKeys(value);

        //Click on required option
        WebElement selectOption = driver.findElement(By.xpath("//ul[contains(@role,'listbox')]//li[@role='option' and normalize-space(@aria-label)='"+value+"']"));
        webDriverCommonLibrary.explicitWait(driver,selectOption);
        webDriverCommonLibrary.scrollIntoTheView(driver,selectOption);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver,selectOption);
    }




}