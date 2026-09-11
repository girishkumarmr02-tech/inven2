package pages;

import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DateExpressionResolver;
import utils.WebDriverCommonLibrary;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


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

        System.out.println("========================================");
        System.out.println("Section : [" + section + "]");
        System.out.println("Field   : [" + field + "]");
        System.out.println("Value   : [" + value + "]");
        System.out.println("========================================");
        WebElement dropdown;

        if (section.equalsIgnoreCase("Product Details")
                && field.equalsIgnoreCase("Product")) {

            dropdown = driver.findElement(By.xpath("//div[@formarrayname='item_details']" + "//tbody/tr[1]" + "/td[2]" + "//p-dropdown" + "//span[@role='combobox']"));
        } else {
            // KEEP YOUR EXISTING WORKING GENERAL LOCATOR
            dropdown = driver.findElement(By.xpath("//p-card[@class='p-element']" + "[.//span[contains(text(),'" + section + "')]]" + "//span[contains(@aria-label,'" + field + "')]"));
        }

        System.out.println("Dropdown located successfully.");
        dropdown.click();
        System.out.println("Dropdown clicked successfully.");

        //search required options
//        WebElement searchboxoption = driver.findElement(By.xpath("//span[@aria-label='"+field+"' and @aria-expanded='true']/following::input[@role='searchbox']"));
        WebElement searchboxoption = driver.findElement(By.xpath("//input[@role='searchbox']"));
        System.out.println("Search box located successfully.");
        webDriverCommonLibrary.explicitWait(driver,searchboxoption);
        searchboxoption.clear();
        searchboxoption.sendKeys(value);
        System.out.println("Entered value in search box: [" + value + "]");


        //Click on required option
//        WebElement selectOption = driver.findElement(By.xpath("//ul[contains(@class,'p-dropdown-items')]//span[normalize-space(text())='"+value+"']"));
        WebElement selectOption = driver.findElement(By.xpath("//li[@role='option' and normalize-space()='" + value + "']"));
        webDriverCommonLibrary.explicitWait(driver,selectOption);
        webDriverCommonLibrary.scrollIntoTheView(driver,selectOption);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver,selectOption);
    }

    public void selectTableDropdownValue(String value, String field, String section) {

        //Find field element
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

    public void
    selectTableType2DropdownValues(String section, String field, String value) {

        //Find field element
        WebElement dropdown = driver.findElement(By.xpath("//h3[normalize-space()='"+section+"']/following::table[1]//tbody/tr[1]/td[count(//h3[normalize-space()='"+section+"']/following::table[1]//thead/tr/th[.//span[normalize-space()='"+field+"']]/preceding-sibling::th)+1]//div[contains(@class,'p-dropdown-trigger')]"));
        webDriverCommonLibrary.scrollIntoTheView(driver,dropdown);
        webDriverCommonLibrary.explicitWait(driver,dropdown);
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
        // After clicking selectOption in the previous dropdown call, before returning:
        webDriverCommonLibrary.explicitWaitInvisibility(driver, By.cssSelector(".p-overlay-mask, .p-dropdown-panel"));
    }

    public void selectLiteralDropdownValue(String section, String field, String value) {
            //Find class element
            WebElement dropdown = driver.findElement(By.xpath("//p-card[@class='p-element'][.//span[contains(text()," + xpathLiteral(section) + ")]]//span[contains(@aria-label," + xpathLiteral(field) + ")]"));dropdown.click();
            webDriverCommonLibrary.waitForSetTime(200);
            //search required options
            WebElement searchboxoption = driver.findElement(By.xpath("//span[@aria-label=" + xpathLiteral(field) + " and @aria-expanded='true']/following::input[@role='searchbox']"));
            webDriverCommonLibrary.explicitWait(driver, searchboxoption);
            searchboxoption.clear();
            searchboxoption.sendKeys(value);
            //Click on required option
            WebElement selectOption = driver.findElement(By.xpath("//ul[contains(@class,'p-dropdown-items')]//span[normalize-space(text())=" + xpathLiteral(value) + "]"));
            webDriverCommonLibrary.explicitWait(driver, selectOption);
            webDriverCommonLibrary.scrollIntoTheView(driver, selectOption);
            webDriverCommonLibrary.clickWithJavaScriptExecutor(driver, selectOption);
        }

/**
 * Safely wraps a raw string as an XPath string literal, handling values
 * that contain single quotes, double quotes, or both — via concat()
 * when necessary. Prevents malformed XPath like:
 *   @aria-label='BUYER'S CALL'   (breaks on the embedded apostrophe)
 */
        String xpathLiteral(String value) {
            if (!value.contains("'")) {
                return "'" + value + "'";
            }
            if (!value.contains("\"")) {
                return "\"" + value + "\"";
            }
            // Contains BOTH quote types — split on ' and rebuild with concat()
            String[] parts = value.split("'", -1);
            StringBuilder sb = new StringBuilder("concat(");
            for (int i = 0; i < parts.length; i++) {
                sb.append("'").append(parts[i]).append("'");
                if (i < parts.length - 1) {
                    sb.append(", \"'\", ");
                }
            }
            sb.append(")");
            return sb.toString();
        }
    @When("user enter {string} in the {string} number field in the {string} table")
    public void userEnterInTheNumberFieldInTheTable(String value, String field, String section) {
        WebElement numberField = driver.findElement(By.xpath("//h3[normalize-space()='"+section+"']/following::table[1]//tbody/tr[1]/td[count(//h3[normalize-space()='"+section+"']/following::table[1]//thead/tr/th[.//span[normalize-space()='"+field+"']]/preceding-sibling::th)+1]//.//input"));
        webDriverCommonLibrary.explicitWait(driver,numberField);
        webDriverCommonLibrary.scrollIntoTheView(driver,numberField);
        numberField.sendKeys(value);
    }

    public void selectTablePicklistType2DropdownValue(String section, String field, String value) {

        // Step 1: resolve the table ONCE, reuse the same reference for both header and body
        WebElement table = driver.findElement(By.xpath(
                "//h3[normalize-space()='" + section + "']/following::table[1]"
        ));

        // Step 2: find the header <th> for this field WITHIN that exact table
        WebElement headerCell = table.findElement(By.xpath(
                ".//thead/tr/th[.//span[normalize-space()='" + field + "']]"
        ));

        // Step 3: compute its column index via JS — reliable even with colspan/frozen quirks,
        // since it reads the browser's own cellIndex rather than recomputing via preceding-sibling count
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long columnIndex = (Long) js.executeScript("return arguments[0].cellIndex;", headerCell);

        // Step 4: find the matching <td> in row 1 of the SAME table, using that index (0-based + 1 for XPath)
        WebElement dropdown = table.findElement(By.xpath(
                ".//tbody/tr[1]/td[" + (columnIndex + 1) + "]//div[contains(@class,'p-dropdown-trigger')]"));



        webDriverCommonLibrary.scrollIntoTheViewNearest(driver, dropdown);
        webDriverCommonLibrary.waitForSetTime(300);
        webDriverCommonLibrary.explicitWait(driver, dropdown);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver, dropdown);

        WebElement selectSearchOption = driver.findElement(By.xpath("//input[@role='searchbox' and @type='text']"));
        webDriverCommonLibrary.scrollIntoTheView(driver, selectSearchOption);
        webDriverCommonLibrary.explicitWaitVisibility(driver, selectSearchOption);
        selectSearchOption.sendKeys(value);

        WebElement selectOption = driver.findElement(By.xpath("//ul[contains(@role,'listbox')]//li[@role='option' and normalize-space(@aria-label)='" + value + "']"));
        webDriverCommonLibrary.explicitWait(driver, selectOption);
        webDriverCommonLibrary.scrollIntoTheView(driver, selectOption);
        webDriverCommonLibrary.clickWithJavaScriptExecutor(driver, selectOption);
        webDriverCommonLibrary.explicitWaitInvisibility(driver, By.cssSelector(".p-overlay-mask, .p-dropdown-panel"));
    }

    public void userSelectsTargetDateRangesFromToForRowFieldInTheTable(String startDateStr, String endDateStr, int rowIndex, String field, String table) {
        LocalDate startDate = DateExpressionResolver.resolve(startDateStr);
        LocalDate endDate = DateExpressionResolver.resolve(endDateStr);
        PageObjectManager.getCreateCommodityContractPage().setTargetDateRange(rowIndex, startDate, endDate);
    }


    public WebElement getVisibleCellByHeaderText(WebDriver driver, String sectionName,
                                                 String columnHeaderText, int rowIndex) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Object result = js.executeScript(
                // Scope to the section container first (heading/legend text == sectionName),
                // THEN search only tables inside that container for the column header.
                //
                // NOTE: header <th> cells on this grid append a separate
                // <span class="mandatory">*</span> for required fields, so raw
                // textContent is "Product*" not "Product". We normalize by
                // stripping '*' and collapsing whitespace before comparing.
                "function normalize(s) { return (s || '').replace(/\\*/g, '').replace(/\\s+/g, ' ').trim(); }" +
                        "let sectionText = arguments[0];" +
                        "let columnHeaderText = normalize(arguments[1]);" +
                        "let rowIndex = arguments[2];" +
                        "let containers = [...document.querySelectorAll('fieldset, .p-panel, section, div')]" +
                        "  .filter(el => {" +
                        "    let heading = el.querySelector('legend, .p-panel-title, h2, h3, h4');" +
                        "    return heading && normalize(heading.textContent) === normalize(sectionText);" +
                        "  });" +
                        "let scopeRoot = containers.length ? containers[0] : document;" +
                        "let tables = [...scopeRoot.querySelectorAll('table')];" +
                        "let table = tables.find(t => [...t.querySelectorAll('thead th')]" +
                        "  .some(th => normalize(th.textContent) === columnHeaderText));" +
                        "if (!table) return null;" +
                        "let targetTh = [...table.querySelectorAll('thead th')]" +
                        "  .find(th => normalize(th.textContent) === columnHeaderText);" +
                        "let thRect = targetTh.getBoundingClientRect();" +
                        "let targetX = thRect.left + thRect.width / 2;" +
                        "let row = table.querySelectorAll('tbody tr')[rowIndex];" +
                        "if (!row) return null;" +
                        "let cells = [...row.querySelectorAll('td')].filter(td => {" +
                        "  let style = td.getAttribute('style') || '';" +
                        "  return !style.includes('display: none') && td.getBoundingClientRect().width > 0;" +
                        "});" +
                        "let best = null, bestDist = Infinity;" +
                        "cells.forEach(td => {" +
                        "  let r = td.getBoundingClientRect();" +
                        "  let center = r.left + r.width / 2;" +
                        "  let dist = Math.abs(center - targetX);" +
                        "  if (dist < bestDist) { bestDist = dist; best = td; }" +
                        "});" +
                        "return best;",
                sectionName, columnHeaderText, rowIndex
        );

        if (result == null) {
            String diagnostic = runDiagnostics(driver, sectionName, columnHeaderText, rowIndex);
            throw new NoSuchElementException(
                    "Could not resolve grid cell for section '" + sectionName +
                            "', column '" + columnHeaderText + "', row " + rowIndex + ".\n" + diagnostic
            );
        }
        return (WebElement) result;
    }

    // ------------------------------------------------------------------
    // Diagnostic dump used ONLY when the primary lookup fails, so the
    // exception tells you exactly which assumption broke instead of a
    // bare "not found". Run this once, read the output, then we know
    // whether it's a row-count problem, a header-text mismatch, or a
    // section-scoping problem — no more guessing.
    // ------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private String runDiagnostics(WebDriver driver, String sectionName, String columnHeaderText, int rowIndex) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<String, Object> diag = (Map<String, Object>) js.executeScript(
                "function normalize(s) { return (s || '').replace(/\\*/g, '').replace(/\\s+/g, ' ').trim(); }" +
                        "let sectionText = arguments[0];" +
                        "let columnHeaderText = normalize(arguments[1]);" +
                        "let containers = [...document.querySelectorAll('fieldset, .p-panel, section, div')]" +
                        "  .filter(el => {" +
                        "    let heading = el.querySelector('legend, .p-panel-title, h2, h3, h4');" +
                        "    return heading && normalize(heading.textContent) === normalize(sectionText);" +
                        "  });" +
                        "let allHeadings = [...document.querySelectorAll('legend, .p-panel-title, h2, h3, h4')]" +
                        "  .map(h => normalize(h.textContent)).filter(t => t.length > 0);" +
                        "let scopeRoot = containers.length ? containers[0] : document;" +
                        "let tables = [...scopeRoot.querySelectorAll('table')];" +
                        "let tableHeaderSets = tables.map(t => [...t.querySelectorAll('thead th')]" +
                        "  .map(th => normalize(th.textContent)));" +
                        "let matchingTable = tables.find(t => [...t.querySelectorAll('thead th')]" +
                        "  .some(th => normalize(th.textContent) === columnHeaderText));" +
                        "let rowCount = matchingTable ? matchingTable.querySelectorAll('tbody tr').length : -1;" +
                        "return {" +
                        "  sectionContainersFound: containers.length," +
                        "  allHeadingsOnPage: allHeadings," +
                        "  scopedToDocument: containers.length === 0," +
                        "  tablesInScope: tables.length," +
                        "  headersPerTable: tableHeaderSets," +
                        "  matchingTableFound: !!matchingTable," +
                        "  rowCountInMatchingTable: rowCount" +
                        "};",
                sectionName, columnHeaderText
        );

        StringBuilder sb = new StringBuilder();
        sb.append("Diagnostic:\n");
        sb.append("  Section containers matching '" + sectionName + "': " + diag.get("sectionContainersFound") + "\n");
        if (Boolean.TRUE.equals(diag.get("scopedToDocument"))) {
            sb.append("  -> No section container matched by heading text; fell back to whole-document search.\n");
            sb.append("     All heading texts found on page: " + diag.get("allHeadingsOnPage") + "\n");
        }
        sb.append("  Tables in scope: " + diag.get("tablesInScope") + "\n");
        sb.append("  Header rows per table found: " + diag.get("headersPerTable") + "\n");
        sb.append("  Table matching column '" + columnHeaderText + "' found: " + diag.get("matchingTableFound") + "\n");
        sb.append("  Row count in that table's tbody: " + diag.get("rowCountInMatchingTable") +
                " (requested rowIndex=" + rowIndex + ")\n");
        int rowCount = ((Number) diag.get("rowCountInMatchingTable")).intValue();
        if (rowCount == 0) {
            sb.append("  -> LIKELY ROOT CAUSE: the grid currently has 0 rows in <tbody>. " +
                    "You need to click 'Add Row' BEFORE filling row 0, not just before rows 1..N.\n");
        }  else if (rowCount >= 0 && rowIndex >= rowCount) {
            sb.append("  -> LIKELY ROOT CAUSE: requested rowIndex is >= actual row count. " +
                    "Add a row before filling this index.\n");
        } else if (!(Boolean) diag.get("matchingTableFound")) {
            sb.append("  -> LIKELY ROOT CAUSE: header text mismatch. Compare exact strings above against '" +
                    columnHeaderText + "' (watch for '*', extra spaces, or icons inside the <th>).\n");
        }
        return sb.toString();
    }

    public void clickGridColumnDropdown(WebDriver driver, String sectionName,String columnHeaderText, int rowIndex) {
        WebElement cell = getVisibleCellByHeaderText(driver, sectionName, columnHeaderText, rowIndex);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", cell);
        WebElement trigger = cell.findElement(By.cssSelector("div.p-dropdown-trigger"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(trigger));
        trigger.click();
    }

    public void selectProductDetailValue(String sectionName, String columnName, int rowIndex, String value) {
        clickGridColumnDropdown(driver, sectionName, columnName, rowIndex);

        WebElement option = driver.findElement(By.xpath("//ul[contains(@class,'p-dropdown-items')]//li[@aria-label='"+value+"']"));
        try{
            webDriverCommonLibrary.clickWithJavaScriptExecutor(driver,option);
        }catch (Exception e){
            webDriverCommonLibrary.clickUsingActionClass(driver,option);
        }
    }

    // Generic dropdown selector (Product, Trade Month, UOM, etc.)
    public void selectDropdownValue(String sectionName, String columnName, int rowIndex, String value) {
        if (value == null || value.trim().isEmpty())
            return;
        selectProductDetailValue(sectionName, columnName, rowIndex, value);
    }

    // For PrimeNG multi-select / secondary dropdown style columns (e.g. Packing)
    public void selectTableType2DropdownValues(String sectionName, String columnName,
                                               int rowIndex, String value) {
        if (value == null || value.trim().isEmpty()) return;
        selectProductDetailValue(sectionName, columnName, rowIndex, value);
    }

    // Numeric input cells (Units, Qty., No Of Lots., Price)
    public void userEnterInTheNumberFieldInTheTable(String columnName, String sectionName,
                                                    int rowIndex, String value) {
        if (value == null || value.trim().isEmpty()) return;

        WebElement cell = getVisibleCellByHeaderText(driver, sectionName, columnName, rowIndex);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", cell
        );

        WebElement input = cell.findElement(By.cssSelector("input"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(input));
        input.clear();
        input.sendKeys(value);
        input.sendKeys(Keys.TAB);
    }

    // Plain text cells (Position Month, Quality) — kept separate from the
    // numeric helper above so callers are explicit about intent, but they no
    // longer both fire for the same column.


    public void enterFieldValue(String columnName, String sectionName,
                                int rowIndex, String value) {
        if (value == null || value.trim().isEmpty()) return;

        WebElement cell = getVisibleCellByHeaderText(driver, sectionName, columnName, rowIndex);
        WebElement input = cell.findElement(By.cssSelector("input"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(input));
        input.clear();
        input.sendKeys(value);
    }

    // Click whatever "+ Add Row" control your grid uses before filling row i > 0.
    // Update the locator to match your actual markup — placeholder shown here.
    public void addProductDetailRow(String sectionName) {

        int rowCountBefore = countProductDetailRows();
        System.out.println("========================================");
        System.out.println("ADD PRODUCT DETAIL ROW");
        System.out.println("Section        : " + sectionName);
        System.out.println("Rows before    : " + rowCountBefore);
        System.out.println("========================================");

        /*
         * IMPORTANT:
         * The Add (+) button may NOT be inside the last <tr>.
         *
         * Therefore we scope to the Product Details section first,
         * then search for the + button anywhere inside that section.
         */
        By addButtonLocator = By.xpath(
                "//div[@formarrayname='item_details']" +
                        "//xbs-dcloud-caption[.//h3[normalize-space()='" + sectionName + "']][1]" +
                        "/following-sibling::div[1]" +
                        "//*[self::button or @role='button']" +
                        "[.//i[contains(@class,'pi-plus')] " +
                        "or .//span[contains(@class,'pi-plus')] " +
                        "or contains(@class,'pi-plus')]"
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {

            System.out.println("Searching Add (+) button...");

            WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(addButtonLocator));

            System.out.println("Add (+) button FOUND.");
            System.out.println("Button HTML:");
            System.out.println(addButton.getAttribute("outerHTML"));

            wait.until(ExpectedConditions.elementToBeClickable(addButtonLocator));

            WebElement freshButton = wait.until(ExpectedConditions.elementToBeClickable(addButtonLocator));

            System.out.println("Button displayed = " + freshButton.isDisplayed());
            System.out.println("Button enabled   = " + freshButton.isEnabled());

            System.out.println("Button class     = " +
                    freshButton.getAttribute("class"));

            System.out.println("Button HTML BEFORE CLICK:");
            System.out.println(freshButton.getAttribute("outerHTML"));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                    freshButton
            );

            webDriverCommonLibrary.waitForSetTime(300);
            System.out.println("Clicking Add (+) button using Selenium click...");
            freshButton.click();

            System.out.println("Selenium click completed.");

             webDriverCommonLibrary.waitForSetTime(200);
            System.out.println("========================================");
            System.out.println("AFTER ADD BUTTON CLICK");
            System.out.println("========================================");

            System.out.println(
                    "Existing row locator count = "
                            + countProductDetailRows()
            );

            debugProductDetailRowCountBroad();

            boolean rowAdded = waitForRowCountToExceed(
                    rowCountBefore,
                    Duration.ofSeconds(8)
            );


            if (!rowAdded) {
                System.out.println("Row count did NOT increase.");
                debugRowValidity(rowCountBefore - 1);
                throw new IllegalStateException(
                        "Add (+) button was clicked, but Product Details row count " +
                                "did not increase. Previous row count = " +
                                rowCountBefore);
            }

            int rowCountAfter = countProductDetailRows();
            System.out.println("========================================");
            System.out.println("ROW ADDED SUCCESSFULLY");
            System.out.println("Rows before : " + rowCountBefore);
            System.out.println("Rows after  : " + rowCountAfter);
            System.out.println("========================================");

        } catch (TimeoutException e) {
            System.out.println("========================================");
            System.out.println("ADD BUTTON NOT FOUND");
            System.out.println("Section : " + sectionName);
            System.out.println("Rows   : " + rowCountBefore);
            System.out.println("========================================");
            debugProductDetailActionButtons(rowCountBefore);

            throw new IllegalStateException(
                    "Add button for section '" + sectionName + "' could not be found within the timeout. " + "The current locator may not match the actual Add button location.", e);
        }
    }
    private int countProductDetailRows() {
            return driver.findElements(productDetailRows).size();
        }


    private boolean waitForRowCountToExceed(int previousCount, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(d -> countProductDetailRows() > previousCount);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void closeAnyOpenOverlay(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("document.body.click();");
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();

        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d ->
                    (Boolean) ((JavascriptExecutor) d).executeScript(
                            "return document.querySelectorAll('.p-overlay-panel, .p-dropdown-panel, .p-datepicker, .cdk-overlay-container .cdk-overlay-pane').length === 0;"));
        } catch (TimeoutException ignored) {}

        // NEW: wait for any blur-triggered Angular change detection / re-render to finish
        ((JavascriptExecutor) driver).executeScript(
                "return new Promise(resolve => setTimeout(() => requestAnimationFrame(() => requestAnimationFrame(resolve)), 150));");
    }
    private final By productDetailRows = By.xpath("//div[@formarrayname='item_details']" + "//xbs-dcloud-caption[.//h3[normalize-space()='Product Details']][1]" + "/following-sibling::div[1]" + "//table//tbody/tr");
    public List<WebElement> getProductDetailRows() {
        return driver.findElements(productDetailRows);
    }

    public WebElement getProductDetailRow(int rowNumber) {
        return driver.findElement(
                By.xpath("(//div[@formarrayname='item_details']" + "//xbs-dcloud-caption[.//h3[normalize-space()='Product Details']][1]" + "/following-sibling::div[1]" + "//table//tbody/tr)[" + rowNumber + "]"));
    }

    // Temporary button
    public void printProductDetailRowCount() {
        List<WebElement> rows = getProductDetailRows();
        System.out.println("================================");
        System.out.println("PRODUCT DETAIL ROW COUNT = " + rows.size());
        System.out.println("================================");
    }

    // Temporary button
    public void debugProductDetailActionButtons(int rowNumber) {

        WebElement row = getProductDetailRow(rowNumber);

        List<WebElement> buttons = row.findElements(
                By.xpath(".//button"));

        System.out.println("========================================");
        System.out.println("ROW NUMBER       = " + rowNumber);
        System.out.println("BUTTON COUNT     = " + buttons.size());
        System.out.println("========================================");

        for (int i = 0; i < buttons.size(); i++) {

            WebElement button = buttons.get(i);

            System.out.println("----------------------------------------");
            System.out.println("BUTTON " + (i + 1));
            System.out.println("text      = [" + button.getText() + "]");
            System.out.println("aria-label= [" + button.getAttribute("aria-label") + "]");
            System.out.println("title     = [" + button.getAttribute("title") + "]");
            System.out.println("class     = [" + button.getAttribute("class") + "]");
            System.out.println("outerHTML = ");
            System.out.println(button.getAttribute("outerHTML"));
            System.out.println("----------------------------------------");
        }
    }

    public void debugProductDetailAddButtons(String sectionName) {

        System.out.println("========================================");
        System.out.println("PRODUCT DETAILS ADD BUTTON DEBUG");
        System.out.println("Section = " + sectionName);
        System.out.println("========================================");

        By sectionLocator = By.xpath("//div[@formarrayname='item_details']" + "//xbs-dcloud-caption[.//h3[normalize-space()='" + sectionName + "']][1]" + "/following-sibling::div[1]");
        List<WebElement> sections = driver.findElements(sectionLocator);
        System.out.println("Section containers found = " + sections.size());

        if (sections.isEmpty()) {
            System.out.println(
                    "ERROR: Product Details section container was NOT found."
            );return;
        }

        WebElement section = sections.get(0);
        List<WebElement> buttons = section.findElements(
                By.xpath(".//button | .//*[@role='button']"));

        System.out.println("Buttons / role=button found = " + buttons.size());
        for (int i = 0; i < buttons.size(); i++) {
            WebElement button = buttons.get(i);
            System.out.println("----------------------------------------");
            System.out.println("BUTTON " + (i + 1));
            System.out.println("Text       = [" + button.getText() + "]");
            System.out.println("aria-label = [" + button.getAttribute("aria-label") + "]");
            System.out.println("title      = [" + button.getAttribute("title") + "]");
            System.out.println("class      = [" + button.getAttribute("class") + "]");
            System.out.println("displayed  = " + button.isDisplayed());
            System.out.println("enabled    = " + button.isEnabled());
            System.out.println("OUTER HTML:");
            System.out.println(button.getAttribute("outerHTML"));
            System.out.println("----------------------------------------");
        }
        System.out.println("END PRODUCT DETAILS ADD BUTTON DEBUG");
        System.out.println("========================================");
    }

    public void debugLastProductDetailRow() {

        List<WebElement> rows = getProductDetailRows();

        System.out.println("========================================");
        System.out.println("TOTAL ROWS = " + rows.size());
        System.out.println("========================================");

        WebElement lastRow = rows.get(rows.size() - 1);

        System.out.println("LAST ROW HTML:");
        System.out.println(lastRow.getAttribute("outerHTML"));
    }

    /**
     * Confirms a numeric table field's value actually committed to Angular's
     * reactive form model, by re-reading it back from the DOM, before we
     * trust the row is in a stable state.
     */
    public boolean verifyTableNumberFieldCommitted(String fieldLabel, String section, int rowIndex, String expectedValue, Duration timeout) {
        int xpathRow = rowIndex + 1; // convert 0-based loop index to 1-based XPath predicate

        By fieldLocator = By.xpath(
                "(//div[@formarrayname='item_details']" +
                        "//xbs-dcloud-caption[.//h3[normalize-space()='" + section + "']][1]" +
                        "/following-sibling::div[1]//table//tbody/tr)[" + xpathRow + "]" +
                        "//xbs-dcloud-input-number[.//label[normalize-space()='" + fieldLabel + "']" +
                        " or ancestor::td[.//label[normalize-space()='" + fieldLabel + "']]]" +
                        "//input");

        try {
            new WebDriverWait(driver, timeout).until(d -> {
                List<WebElement> els = d.findElements(fieldLocator);
                if (els.isEmpty()) return false;
                String actual = els.get(0).getAttribute("value");
                return actual != null && actual.trim().equals(expectedValue.trim());
            });
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void debugRowValidity(int rowIndex) {
        int xpathRow = rowIndex + 1;
        List<WebElement> controls = driver.findElements(By.xpath(
                "(//div[@formarrayname='item_details']" +
                        "//xbs-dcloud-caption[.//h3[normalize-space()='Product Details']][1]" +
                        "/following-sibling::div[1]//table//tbody/tr)[" + xpathRow + "]" +
                        "//input | (//div[@formarrayname='item_details']" +
                        "//xbs-dcloud-caption[.//h3[normalize-space()='Product Details']][1]" +
                        "/following-sibling::div[1]//table//tbody/tr)[" + xpathRow + "]//textarea"));
        System.out.println("=== ROW " + rowIndex + " FIELD VALIDITY ===");
        for (WebElement c : controls) {
            String cls = c.getAttribute("class");
            System.out.println(
                    "id=" + c.getAttribute("id") +
                            " value=[" + c.getAttribute("value") + "]" +
                            " invalid=" + (cls != null && cls.contains("ng-invalid")) +
                            " untouched=" + (cls != null && cls.contains("ng-untouched")));
        }
    }

    public String getUomFieldValue(String section, int rowIndex) {
        int xpathRow = rowIndex + 1;
        List<WebElement> els = driver.findElements(By.xpath(
                "(//div[@formarrayname='item_details']" +
                        "//xbs-dcloud-caption[.//h3[normalize-space()='" + section + "']][1]" +
                        "/following-sibling::div[1]//table//tbody/tr)[" + xpathRow + "]" +
                        "//xbs-dcloud-input-text[.//label[normalize-space()='UOM']]//input"
        ));
        return els.isEmpty() ? "" : els.get(0).getAttribute("value");
    }

    public void debugAllProductDetailTables() {

        System.out.println("========================================");
        System.out.println("PRODUCT DETAIL TABLE DOM DEBUG");
        System.out.println("========================================");

        List<WebElement> tables = driver.findElements(
                By.xpath(
                        "//div[@formarrayname='item_details']" +
                                "//table"
                )
        );

        System.out.println("Product Details tables found = " + tables.size());

        for (int i = 0; i < tables.size(); i++) {

            WebElement table = tables.get(i);

            List<WebElement> rows = table.findElements(
                    By.xpath(".//tbody/tr")
            );

            System.out.println("----------------------------------------");
            System.out.println("TABLE " + (i + 1));
            System.out.println("Rows = " + rows.size());

            for (int r = 0; r < rows.size(); r++) {

                System.out.println("ROW " + (r + 1) + " displayed=" + rows.get(r).isDisplayed());
            }
            System.out.println("----------------------------------------");
        }
        System.out.println("========================================");
    }

    public int debugProductDetailRowCountBroad() {

        By locator = By.xpath(
                "//div[@formarrayname='item_details']" +
                        "//h3[normalize-space()='Product Details']" +
                        "/ancestor::*[self::div or self::section][1]" +
                        "//table//tbody/tr"
        );

        int count = driver.findElements(locator).size();
        System.out.println("BROAD PRODUCT DETAIL ROW COUNT = " + count);
        return count;
    }

  }