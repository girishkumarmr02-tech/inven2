package utils;

import io.cucumber.core.options.Constants;
import io.cucumber.java.it.Date;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Function;

import static io.cucumber.core.options.Constants.*;
import static java.lang.Thread.*;

public class WebDriverCommonLibrary {

    private final WebDriver driver;

    public WebDriverCommonLibrary(WebDriver driver) {
        this.driver = driver;

    }

    public WebElement explicitWait(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
        //elementtobeclickable covers both isdisplayed() and isenabled().
    }

    public WebElement explicitWaitByLocator(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement explicitWaitVisibilityByLocator(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    public WebElement explicitWaitVisibility(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void scrollIntoTheView(final WebDriver driver, final WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'nearest'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", element);
    }

    public void highLightTheElement(final WebDriver driver, final WebElement ele) {
        JavascriptExecutor jsExe = (JavascriptExecutor) driver;
        scrollIntoTheView(driver, ele);
        String originalStyle = ele.getAttribute("style");
        jsExe.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", ele);
        dynamicWait(ConfigReader.getHighlightWaitMillis());
        jsExe.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele, originalStyle == null ? "" : originalStyle);
    }

    private void dynamicWait(long millis) {
        try {
            sleep(millis);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    public void clickWithJavaScriptExecutor(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickUsingActionClass(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        scrollIntoTheView(driver,element);
        actions.click(element).build().perform();
    }

    public void waitForOverlayToClose(WebDriver driver, WebElement triggerElement) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);

        // 1. Wait for the trigger's aria-expanded to go back to false
        fluentWait.until((Function<WebDriver, Boolean>) drv -> {
            try {
                String expanded = triggerElement.getAttribute("aria-expanded");
                return "false".equals(expanded);
            } catch (StaleElementReferenceException e) {
                return true; // element was replaced/detached — treat as closed
            }
        });

        // 2. Wait for the listbox overlay to detach/hide from the DOM
        fluentWait.until((Function<WebDriver, Boolean>) drv -> {
            java.util.List<WebElement> panels = drv.findElements(
                    By.xpath("//ul[contains(@role,'listbox')]"));
            return panels.isEmpty() || panels.stream().noneMatch(WebElement::isDisplayed);
        });
    }

    public void selectTargetDateRangeInTable(String fieldLabel,String tableName,LocalDate startDate, LocalDate endDate) {

        WebDriverCommonLibrary webDriverCommonLibrary = new WebDriverCommonLibrary(driver);
        // Locate & open the date-range input (reuses your section/field counting pattern)
        By inputLocator = By.xpath(buildTableFieldInputXpath(tableName, fieldLabel));
        WebElement dateRangeInput = webDriverCommonLibrary.explicitWaitByLocator(driver, inputLocator);
        webDriverCommonLibrary.scrollIntoTheView(driver, dateRangeInput);
        dateRangeInput.click();

        // Read the dynamically generated overlay panel id — never hardcode pn_id_*
        String panelId = dateRangeInput.getAttribute("aria-controls");
        By panelLocator = By.id(panelId);
        webDriverCommonLibrary.explicitWaitVisibilityByLocator(driver, panelLocator);

        // Navigate + click start date
        navigateCalendarToMonth(panelId, startDate);
        clickCalendarDate(panelId, startDate);

        // Navigate + click end date (panel stays open in range selectionMode)
        navigateCalendarToMonth(panelId, endDate);
        clickCalendarDate(panelId, endDate);

        closeCalendarIfOpen(panelLocator);
    }

    private String buildTableFieldInputXpath(String tableName, String fieldLabel) {
        return "//h3[normalize-space()='" + tableName + "']/following::table[1]//tbody/tr[1]/td["
                + "count(//h3[normalize-space()='" + tableName + "']/following::table[1]//thead/tr/th"
                + "[.//span[normalize-space()='" + fieldLabel + "']]/preceding-sibling::th)+1]"
                + "//input[@role='combobox']";
    }

    private void navigateCalendarToMonth(String panelId, LocalDate targetDate) {
        WebDriverCommonLibrary webDriverCommonLibrary = new WebDriverCommonLibrary(driver);

        YearMonth target = YearMonth.of(targetDate.getYear(), targetDate.getMonth());
        int maxAttempts = 24; // safety guard against infinite loop

        for (int i = 0; i < maxAttempts; i++) {
            WebElement monthEl = driver.findElement(By.xpath("//*[@id='" + panelId + "']//span[contains(@class,'p-datepicker-month')]"));
            WebElement yearEl  = driver.findElement(By.xpath("//*[@id='" + panelId + "']//span[contains(@class,'p-datepicker-year')]"));

            YearMonth current = YearMonth.of(
                    Integer.parseInt(yearEl.getText().trim()),
                    Month.valueOf(monthEl.getText().trim().toUpperCase())
            );

            if (current.equals(target)) return;

            String navLabel = current.isBefore(target) ? "Next Month" : "Previous Month";
            By navBtn = By.xpath("//*[@id='" + panelId + "']//button[@aria-label='" + navLabel + "']");
            webDriverCommonLibrary.explicitWaitByLocator(driver, navBtn).click();
        }
        throw new RuntimeException("Could not navigate calendar to " + target);
    }

    private void clickCalendarDate(String panelId, LocalDate date) {
        WebDriverCommonLibrary webDriverCommonLibrary = new WebDriverCommonLibrary(driver);

        int primeNgMonthIndex = date.getMonthValue() - 1; // zero-indexed, per data-date attribute
        String dataDateValue = date.getYear() + "-" + primeNgMonthIndex + "-" + date.getDayOfMonth();

        By dateLocator = By.xpath(
                "//*[@id='" + panelId + "']//span[@data-date='" + dataDateValue + "'"
                        + " and not(ancestor::td[contains(@class,'p-datepicker-other-month')])]"
        );
        webDriverCommonLibrary.explicitWaitByLocator(driver, dateLocator).click();
    }

    private void closeCalendarIfOpen(By panelLocator) {
         List<WebElement> panels = driver.findElements(panelLocator);
        if (!panels.isEmpty() && panels.get(0).isDisplayed()) {
            driver.findElement(By.tagName("body")).click(); // click outside to force-close
        }
    }
    public void testselectTargetDateRangeInTable(String fieldLabel,String tableName,LocalDate startDate, LocalDate endDate) {

        WebDriverCommonLibrary webDriverCommonLibrary = new WebDriverCommonLibrary(driver);
        By targetDateRange = By.xpath("//p-calendar[@selectionmode='range']" + "//input[@role='combobox' and @aria-haspopup='dialog']");
        WebElement targetDateRangeElement = webDriverCommonLibrary.explicitWaitByLocator(driver, targetDateRange);
        webDriverCommonLibrary.scrollIntoTheView(driver,targetDateRangeElement);
        targetDateRangeElement.click();

    }
}