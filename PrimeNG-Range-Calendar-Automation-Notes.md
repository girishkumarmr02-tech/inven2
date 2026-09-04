# PrimeNG Range Calendar Automation — Complete Implementation Notes
### Target Field: "Target Date Range" — Product Details Section — CCM Module (ERP-Automation)

---

## STEP 1 — DOM / Component Analysis

**Component identified:** PrimeNG `p-calendar` in range-selection mode:
```html
<p-calendar selectionmode="range" appendto="body" class="p-element p-inputwrapper w-full ...">
```

### Element roles

| Element | Role | Notes |
|---|---|---|
| `<xbs-dcloud-date-range>` | Custom Angular wrapper host | Not interactable — structural only |
| `<p-calendar>` | PrimeNG component host | Not interactable — component selector, not real control |
| `<span class="p-calendar p-input-icon-right">` | Layout wrapper | Not interactable |
| `<input type="text" role="combobox" aria-haspopup="dialog" readonly>` | The date-range text field | `readonly` — cannot type into it |
| `<calendaricon class="... p-datepicker-icon ...">` | Calendar trigger icon | Custom element wrapping an `<svg>` |

### Popup rendering behavior (from visual inspection)
- Single-month view (not dual-panel)
- Header: `<` prev, month+year, `>` next
- Day grid: Su–Sa columns
- Greyed numbers = adjacent-month overflow days
- Grey circle = today; Blue circles = selected range

### Attributes to TRUST (stable)
- `role="combobox"`, `aria-haspopup="dialog"`
- `selectionmode="range"` on `p-calendar`
- `<th>`/`<span>` text `"Target Date Range"`
- `appendto="body"` — confirms popup renders OUTSIDE the `<td>`, requires two-scope locator strategy

### Attributes to NEVER TRUST (unstable)
- `class="... ng-tns-c1685646730-114 ..."` — Angular auto-generated, changes per render instance (confirmed: same logical icon had `-114`, `-278`, `-62`, `-276/277/279` across different captures in this session)
- `id="pn_id_261"` style IDs — auto-incremented per load order
- `class="even"` on `<table>` — NOT zebra-striping; confirmed to indicate **one separate `<table>` per product-line row**

### CRITICAL STRUCTURAL DISCOVERY (confirmed via console)
```javascript
document.querySelectorAll("h3.caption").length        // → 3
document.querySelectorAll("table.even, table.odd").length // → 3
document.querySelectorAll('div[formarrayname="item_details"]').length // → 1
```

**Conclusion:** The DOM is a single `<div formarrayname="item_details">` (Angular reactive FormArray container) with **repeating flat sibling blocks**, each block = one `<xbs-dcloud-caption><h3>Product Details</h3></xbs-dcloud-caption>` + its own `<div><div><table class="even/odd">`. There is NOT one table with multiple `<tbody><tr>` rows.

```html
<div formarrayname="item_details">
  <!-- Row 1 -->
  <xbs-dcloud-caption><h3 class="caption">Product Details</h3></xbs-dcloud-caption>
  <div class="ng-star-inserted"><div><table class="even">...</table></div></div>
  <!-- Row 2 -->
  <xbs-dcloud-caption><h3 class="caption">Product Details</h3></xbs-dcloud-caption>
  <div class="ng-star-inserted"><div><table class="odd">...</table></div></div>
  <!-- Row 3 -->
  ...
</div>
```

**Impact:** A naive `//h3[...]/following::table[1]` ALWAYS resolves to Row 1's table regardless of which `h3` you started from, because `following::` searches the entire document in source order, not "next table relative to this specific h3." This was the root bug behind several earlier failed locator attempts in this framework.

---

## STEP 2 — Locator Strategy Comparison (summary)

| # | Strategy | Verdict |
|---|---|---|
| 1 | ID | Not usable — no `id` on target elements |
| 2 | Name | Not usable — no `name` attribute present |
| 3 | CSS Selector | Cannot express row-index/column-position logic — rejected as primary |
| 4 | Absolute XPath | Rejected — extremely brittle |
| 5 | Relative XPath (semantic) | **Recommended primary strategy** |
| 6 | Parent-Child (`xbs-dcloud-date-range`) | Used as a supporting safety-net fragment, not standalone |
| 7 | Label-based (`<th>` text) | Incorporated into strategy 5 (column resolution) |
| 8 | ARIA/Role-based | Not usable alone (no `aria-label` tying input to column); used as a disambiguation filter only |
| 9 | PrimeNG-specific (`p-datepicker-icon`, `role='combobox'`) | **Used as the final leaf-node selector** |
| 10 | Dynamic table-column (`count(preceding-sibling::th)+1`) | **Required — solves column position dynamically** |

**Winning composed approach** = Row scope (strategy 5) + Column resolution (strategy 10) + Cell-type safety net (strategy 6) + Leaf element (strategy 9).

---

## STEP 3 — Dynamic Table Locator (full breakdown)

### Part A — Row scope
```xpath
(//div[@formarrayname='item_details']//xbs-dcloud-caption[.//h3[normalize-space()='Product Details']])[$rowIndex]
```
- `normalize-space()` guards against stray whitespace in Angular-interpolated text
- Outer parentheses are mandatory: `(...)[$rowIndex]` evaluates the full node-set THEN indexes it — different from `[...][$rowIndex]` chained directly on the predicate

### Part B — Caption → its own table
```xpath
/following-sibling::div[1]//table
```
- `following-sibling::` (NOT `following::`) — restricts to true siblings under the same parent, fixing the Step 1 bug
- `[1]` is justified because the table-wrapping div is the CONFIRMED immediate next sibling (not an arbitrary guess)

### Part C — Column position (dynamic, no hardcoding)
```xpath
count(<rowScope>//thead/tr/th[.//span[normalize-space()='Target Date Range']]/preceding-sibling::th) + 1
```
- `preceding-sibling::th` restricts count to header cells only (ignores stray Angular `<!---->` comment nodes)
- `count()` converts node-set → number; `+1` converts zero-based sibling count → 1-based XPath position

### Part D — Apply position to that row's tbody
```xpath
//tbody/tr[1]/td[ <computed position> ]
```
`tr[1]` is safe here because EACH row has its OWN table with a single data row (row-multiplicity was already resolved in Part A).

### Part E — Leaf element
```xpath
.//xbs-dcloud-date-range//calendaricon[contains(@class,'p-datepicker-icon')]
```

### Full single-line XPath (icon)
```xpath
//div[@formarrayname='item_details']//xbs-dcloud-caption[.//h3[normalize-space()='Product Details']][$rowIndex]/following-sibling::div[1]//table//tbody/tr[1]/td[count(//div[@formarrayname='item_details']//xbs-dcloud-caption[.//h3[normalize-space()='Product Details']][$rowIndex]/following-sibling::div[1]//table//thead/tr/th[.//span[normalize-space()='Target Date Range']]/preceding-sibling::th)+1]//xbs-dcloud-date-range//calendaricon[contains(@class,'p-datepicker-icon')]
```

**Note:** In Java, this should be built as TWO separate steps (row-scope computed once, reused), not one giant string — pure XPath 1.0 has no variable-binding, so the row-scope fragment must be duplicated inside `count()`. See Step 13 code for clean implementation.

### Verified against real header (Image 4 dump)
Preceding `<th>` count before "Target Date Range" = 8 (Lot No.[hidden] + Product + Units + Packing + Qty. + UOM + No Of Lots. + Price + Target Type) → column position = 9. Confirmed against full row `<td>` dump.

---

## STEP 4 — Identify the Actual Clickable Element

Three candidates evaluated: `<p-calendar>` (host, REJECTED — not a real interactive element), `<input readonly>` (viable but uncertain click-binding), `<calendaricon>` (RECOMMENDED).

**Decision: Target `<calendaricon class="p-datepicker-icon">` as primary trigger.**

Reasoning:
1. PrimeNG's toggle behavior is most reliably bound to the icon across versions (works even in configurations without visible input click-to-open)
2. `p-datepicker-icon` is a stable framework class, unlike `ng-tns-*` classes on the input's span wrapper
3. Reduces one variable given the already-complex `appendTo="body"` overlay chain

Fallback (input click) documented as available but not implemented — no evidence required it.

---

## STEP 5 — Selenium Wait Strategy

| Condition | Verdict for this use case |
|---|---|
| `presenceOfElementLocated` | Insufficient alone — root cause of earlier `ElementNotInteractableException` bugs (element present but not clickable) |
| `visibilityOfElementLocated` | Good intermediate check; doesn't guarantee enabled/unobscured |
| `visibilityOf(WebElement)` | AVOID — requires caching a `WebElement`, risks staleness across Angular re-renders |
| `elementToBeClickable` | **Correct condition for the icon click** — visible + enabled |

**Why NOT `Thread.sleep()`:** Angular change-detection timing is non-deterministic (varies with machine load, concurrent component re-renders in this heavily-reactive form). Fixed sleeps either waste time or still flake under load — `elementToBeClickable` polls instead of guessing a duration.

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
wait.until(ExpectedConditions.elementToBeClickable(calendarIconLocator));
```

15s timeout justified by the form's multiple cascading/interdependent dropdown validations (not arbitrary).

**Always pass `By`, never a cached `WebElement`**, into `wait.until(...)` — ensures re-query on every poll cycle.

---

## STEP 6 — Click the Calendar

### Risks specific to this DOM (analyzed, not assumed)
1. **SVG hit-testing** inside `<calendaricon>` — LOW risk with W3C-compliant ChromeDriver 4.47.0 (uses bounding-box center, not literal pixel)
2. **Overlapping/scroll clipping** — CONFIRMED real risk: the table's ancestor div has `overflow-x: auto`, and the calendar icon's column may be outside the current scroll viewport

### Implementation
```java
public void clickCalendarIcon(WebDriver driver, By calendarIconLocator) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    WebElement icon = wait.until(ExpectedConditions.presenceOfElementLocated(calendarIconLocator));

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", icon
    );

    WebElement clickableIcon = wait.until(ExpectedConditions.elementToBeClickable(calendarIconLocator));

    try {
        clickableIcon.click();
    } catch (ElementClickInterceptedException e) {
        // Documented fallback for the confirmed overflow-x:auto scroll-container risk only
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableIcon);
    }
}
```

- `scrollIntoView({block:'center', inline:'center'})` handles BOTH vertical and horizontal nested scroll containers — needed because native Selenium/Actions scroll doesn't reliably handle nested horizontal overflow
- Re-wait for `elementToBeClickable` AFTER scroll (scroll can trigger Angular layout recalculation)
- JS-click is a narrow, documented fallback for `ElementClickInterceptedException` ONLY — not a blanket default; native click is always attempted first

---

## STEP 7 — Calendar Popup HTML Analysis (fully empirically confirmed)

### Container structure (confirmed via console enumeration)
```
div.p-datepicker.p-component                    ← outer popup (class also includes
                                                     ng-trigger-overlayAnimation — popup DOES animate in)
  div.p-datepicker-group-container
    div.p-datepicker-group
      div.p-datepicker-header
        button.p-datepicker-prev
          svg.p-datepicker-prev-icon
        div.p-datepicker-title
          button.p-datepicker-month   ← text "September" (separate clickable button, NOT combined text)
          button.p-datepicker-year    ← text "2026" (separate clickable button)
        button.p-datepicker-next
          svg.p-datepicker-next-icon
      div.p-datepicker-calendar-container
        table.p-datepicker-calendar
          tbody > tr > td[aria-label="N"]
            span[data-date="YYYY-M-D"]   ← MONTH IS ZERO-INDEXED (confirmed: Sept = "8")
      div.p-datepicker-buttonbar (Today / Clear)
```

### Day cell states (all empirically confirmed, none assumed)

| State | `<td>` class | `<span>` class |
|---|---|---|
| Adjacent-month overflow | `p-datepicker-other-month` | — |
| Today (unselected) | `p-datepicker-today` | `p-highlight p-datepicker-current-day` |
| Range START (e.g. day 22) | plain, no special class | `p-highlight p-datepicker-current-day` |
| Range END (e.g. day 23) | plain, no special class | `p-highlight p-datepicker-current-day` — **IDENTICAL to start** |

### CRITICAL FINDING
**No distinct "range-start" / "range-end" / "in-range" class exists anywhere in this PrimeNG version.** Confirmed via full-document scan:
```javascript
[...document.querySelectorAll('[class*="range"]')].map(el => el.className)
// → [] (empty array, length 0)
```

**Implication for Steps 8–10:**
- Date selection must use `data-date` value matching (unambiguous), NOT class-based "find the start/end marker"
- Verification (Step 10) MUST use the bound input's `value` attribute as source of truth — popup DOM cannot distinguish start vs end after the fact

### Accessibility element (ignore, not interactable)
```html
<div aria-live="polite" class="p-hidden-accessible ...">22</div>
```

---

## STEP 8 — Start Date Selection

### 1. Read displayed month/year (two independent buttons, not one string)
```java
private YearMonth getDisplayedMonthYear(WebDriver driver) {
    WebElement monthBtn = driver.findElement(By.cssSelector("button.p-datepicker-month"));
    WebElement yearBtn = driver.findElement(By.cssSelector("button.p-datepicker-year"));
    Month month = Month.valueOf(monthBtn.getText().trim().toUpperCase(Locale.ENGLISH));
    int year = Integer.parseInt(yearBtn.getText().trim());
    return YearMonth.of(year, month);
}
```

### 2. Compare with target
```java
int monthsToMove = (int) displayedMonth.until(targetMonth, ChronoUnit.MONTHS);
```

### 3. Navigate (safe prev/next loop — month/year fast-jump buttons NOT used, since their sub-view behavior was never empirically confirmed)
```java
private void navigateToMonth(WebDriver driver, YearMonth target) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    By prevBtn = By.cssSelector("button.p-datepicker-prev");
    By nextBtn = By.cssSelector("button.p-datepicker-next");
    int maxIterations = 60; // 5-year safety cap
    int iterations = 0;

    while (iterations < maxIterations) {
        YearMonth displayed = getDisplayedMonthYear(driver);
        if (displayed.equals(target)) return;

        By navButton = displayed.isBefore(target) ? nextBtn : prevBtn;
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(navButton));
        btn.click();

        // Wait for header to actually change — prevents reading stale value mid re-render
        wait.until(d -> !getDisplayedMonthYear(driver).equals(displayed));
        iterations++;
    }
    throw new NoSuchElementException("Could not navigate calendar to target month/year: " + target);
}
```

### 4. Day-cell locator (data-date based, NOT visible text — avoids adjacent-month ambiguity)
```java
private By dayCellLocator(LocalDate date) {
    String dataDateValue = date.getYear() + "-" + (date.getMonthValue() - 1) + "-" + date.getDayOfMonth();
    return By.xpath(
        "//td[not(contains(@class,'p-datepicker-other-month'))]//span[@data-date='" + dataDateValue + "']"
    );
}
```
- `not(contains(@class,'p-datepicker-other-month'))` kept as defensive redundancy (data-date is already unique) — if it ever excludes a match, that signals a navigation bug, not a functional necessity
- Disabled-date handling: NOT implemented — no `disabled`/`p-disabled` class observed in any capture; flagged as a known gap, to be added only once real evidence (HTML capture) is provided

### 5. Click start date
```java
public void selectStartDate(WebDriver driver, LocalDate startDate) {
    navigateToMonth(driver, YearMonth.from(startDate));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement startCell = wait.until(ExpectedConditions.elementToBeClickable(dayCellLocator(startDate)));
    startCell.click();
}
```
Native click used — no evidence of interception risk on day cells (unlike the icon's confirmed scroll-container risk).

---

## STEP 9 — End Date Selection

**Design principle:** Always re-navigate before clicking end date, regardless of whether the panel auto-advances after start-date selection. This is safe under both possible PrimeNG behaviors:
- If it DID auto-advance → `navigateToMonth`'s `if (displayed.equals(target)) return;` makes this a harmless no-op
- If it DIDN'T auto-advance → navigation proceeds correctly

```java
public void selectEndDate(LocalDate endDate) {
    navigateToMonth(YearMonth.from(endDate));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement endCell = wait.until(ExpectedConditions.elementToBeClickable(dayCellLocator(endDate)));
    endCell.click();
}

public void selectDateRange(LocalDate startDate, LocalDate endDate) {
    selectStartDate(startDate);
    selectEndDate(endDate);
}
```

**Always fresh `findElement`/wait** — never reuse a `WebElement` reference across the navigation call, since month navigation rebuilds the entire day-grid DOM (same stale-reference principle as elsewhere in this framework).

**Chronological order assumption:** Not defensively validated here (`startDate.isBefore(endDate)`) — that's a test-data correctness concern belonging in the step definition/test data layer, not the Selenium interaction layer.

**Popup auto-close after second click:** Not empirically confirmed for this app instance — addressed as a Step 11 real-world-failure consideration, not assumed.

---

## STEP 10 — Verify the Selected Range

### Why input value, not popup DOM
Step 7 proved no start/end distinguishing class exists — the bound `<input>` value is the only reliable source of truth (also reflects actual form-control/submission state, not just "Selenium clicked two cells").

### CONFIRMED exact format (via live console inspection — NOT assumed from screenshot)
```javascript
[...document.querySelectorAll('input[role="combobox"]')].map(el => el.value)
// → ['04-09-2026', '', '22-09-2026 - 23-09-2026', 'Sep-2026', '']
```
Format confirmed: **`dd-MM-yyyy - dd-MM-yyyy`** (index 2 = Target Date Range; other indices are unrelated fields — confirms a generic `input[role='combobox']` selector is NOT sufficient in Selenium; must use Step 3's row/column-scoped locator with `input[@role='combobox']` as the leaf instead of `calendaricon`).

### Implementation
```java
public void verifySelectedDateRange(By dateRangeInputLocator, LocalDate expectedStart, LocalDate expectedEnd) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(dateRangeInputLocator));

    String actualValue = input.getAttribute("value");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String expectedValue = expectedStart.format(formatter) + " - " + expectedEnd.format(formatter);

    Assert.assertEquals(actualValue, expectedValue,
        "Target Date Range value mismatch. Expected: " + expectedValue + ", Actual: " + actualValue);
}
```
- `visibilityOfElementLocated` used (not `elementToBeClickable`) — reading state, not clicking
- Exact `assertEquals` (not `.contains()`) — catches subtler format bugs (e.g. wrong separator/spacing) that a substring check would miss
- Input locator reuses Step 3's row/column scoping chain with `input[@role='combobox']` as the leaf fragment instead of `calendaricon`

---

## STEP 11 — Real-World Failure Catalog

| # | Failure | Cause (grounded in this DOM) | Fix |
|---|---|---|---|
| 1 | `ElementNotInteractableException` | Locator resolves to a label/wrapper `<span>` instead of the real interactive leaf | Always terminate locators on confirmed-interactive elements (input/button/calendaricon), never a bare text span |
| 2 | `InvalidElementStateException` | Attempting `sendKeys()` on the `readonly` input | NEVER call `sendKeys()` on this field — click-only interaction |
| 3 | `ElementClickInterceptedException` | Confirmed: table's `overflow-x:auto` may clip/cover the icon | `scrollIntoView` + JS-click fallback (Step 6) |
| 4 | `StaleElementReferenceException` | Reusing `WebElement` across month navigation / Angular re-render | Always re-locate via `By`, never cache across mutating actions |
| 5 | `TimeoutException` | Icon click silently failed, or header genuinely isn't changing at a navigation boundary | `maxIterations=60` safety cap converts hang → fast, diagnosable `NoSuchElementException` |
| 6 | `NoSuchElementException` | Row/column XPath math wrong, or day clicked before month navigation completed | `navigateToMonth()` always called immediately before `dayCellLocator()` resolution |
| 7 | Overlay not visible (animation) | Confirmed: popup has `ng-trigger-overlayAnimation` — it DOES animate in | Explicit `visibilityOfElementLocated(POPUP_CONTAINER)` wait right after icon click, before any day interaction |
| 8 | Date not clickable | Overlaps with #1/#3; rare here given simple day-cell DOM | Covered by `elementToBeClickable` + #7's fix |
| 9 | Disabled date | NOT observed in any capture for this field | No handling implemented — flagged gap; needs a real HTML capture before adding a class-based exclusion |
| 10 | Angular DOM re-render | Same mechanism as #4 | Same fix |
| 11 | Overlay appended outside table (`appendTo="body"`) | Confirmed from Step 1 | Icon locator (page-scoped) and popup locators (helper-scoped) are always structurally SEPARATE XPath expressions |
| 12 | Month navigation failure | Header text parsing mismatch | `Month.valueOf(monthText.trim().toUpperCase(Locale.ENGLISH))` |
| 13 | Duplicate day numbers / adjacent-month dates | Text-only locators are ambiguous | Solved via `data-date` attribute matching instead of visible text |
| 14 | Calendar animation | Same as #7 | Same fix |
| 15 | Dynamically generated classes | `ng-tns-*` changes per render (confirmed multiple times) | All locators use stable framework classes/semantic attributes only |

**New addition from this step:** `waitForPopupVisible()` — inserted between icon click (Step 6) and date navigation/click (Step 8).

---

## STEP 12 — Page Object Model Design

### Layered architecture
```
StepsContainer.java
  → parses Gherkin params, converts String → LocalDate, calls ONE page method, NO XPath

CreateCommodityContractPage.java (Page Object)
  → owns row/column XPath (Step 3), field-name→column-label mapping,
    orchestrates icon-click→delegate-to-helper, owns input-value verification (Step 10)

PrimeNgCalendarRangeHelper.java (reusable utility)
  → generic to ANY PrimeNG range calendar — NOT CCM-specific
  → popup visibility wait, month/year read+navigate, day-cell locate+click
  → does NOT know about tables/rows/"Target Date Range"

WebDriverCommonLibrary.java (existing, unchanged)
  → generic wait wrappers, scroll, JS-click fallback — helper calls into this
```

**Why the helper doesn't know about rows/columns:** keeps it reusable for any future range-calendar field anywhere in the app, mirroring the existing framework's separation of concerns.

### Method signatures
```java
// PrimeNgCalendarRangeHelper
public PrimeNgCalendarRangeHelper(WebDriver driver, WebDriverCommonLibrary lib);
public void openCalendar(By calendarIconLocator);
public void selectDateRange(LocalDate startDate, LocalDate endDate);

// CreateCommodityContractPage
public void setTargetDateRange(int rowIndex, LocalDate startDate, LocalDate endDate);
private By buildDateRangeLocator(int rowIndex, String leafXPathFragment);
private void verifyTargetDateRange(int rowIndex, LocalDate start, LocalDate end);
```

### Test data flow
```
Feature file (Gherkin) → String dates (ISO yyyy-MM-dd)
  → StepsContainer parses to LocalDate
  → Page Object receives LocalDate + rowIndex, never touches raw strings again
  → XPath only ever contains the COMPUTED data-date value, never a literal test string
```

---

## STEP 13 — Final Production Implementation

### 1. `PrimeNgCalendarRangeHelper.java`
```java
package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Locale;

/**
 * Reusable helper for interacting with PrimeNG range-mode calendars
 * (p-calendar[selectionmode="range"], single-month-panel variant, appendTo="body").
 * Not tied to any specific page/table — the calling Page Object supplies the
 * icon locator that opens the calendar; this class only handles what happens
 * once the popup is open.
 */
public class PrimeNgCalendarRangeHelper {

    private static final By POPUP_CONTAINER = By.cssSelector("div.p-datepicker.p-component");
    private static final By MONTH_BUTTON = By.cssSelector("button.p-datepicker-month");
    private static final By YEAR_BUTTON = By.cssSelector("button.p-datepicker-year");
    private static final By PREV_BUTTON = By.cssSelector("button.p-datepicker-prev");
    private static final By NEXT_BUTTON = By.cssSelector("button.p-datepicker-next");

    private static final int MAX_MONTH_NAVIGATION_ITERATIONS = 60; // 5 years, safety cap
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    private final WebDriver driver;
    private final WebDriverCommonLibrary webDriverCommonLibrary;
    private final WebDriverWait wait;

    public PrimeNgCalendarRangeHelper(WebDriver driver, WebDriverCommonLibrary webDriverCommonLibrary) {
        this.driver = driver;
        this.webDriverCommonLibrary = webDriverCommonLibrary;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    public void openCalendar(By calendarTriggerLocator) {
        WebElement trigger = wait.until(ExpectedConditions.presenceOfElementLocated(calendarTriggerLocator));
        webDriverCommonLibrary.scrollIntoTheView(driver, trigger);

        WebElement clickableTrigger = wait.until(ExpectedConditions.elementToBeClickable(calendarTriggerLocator));
        try {
            clickableTrigger.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableTrigger);
        }

        waitForPopupVisible();
    }

    public void selectDateRange(LocalDate startDate, LocalDate endDate) {
        selectStartDate(startDate);
        selectEndDate(endDate);
    }

    private void waitForPopupVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(POPUP_CONTAINER));
    }

    private void selectStartDate(LocalDate startDate) {
        navigateToMonth(YearMonth.from(startDate));
        clickDay(startDate);
    }

    private void selectEndDate(LocalDate endDate) {
        navigateToMonth(YearMonth.from(endDate));
        clickDay(endDate);
    }

    private void clickDay(LocalDate date) {
        By locator = dayCellLocator(date);
        WebElement dayCell = wait.until(ExpectedConditions.elementToBeClickable(locator));
        dayCell.click();
    }

    private By dayCellLocator(LocalDate date) {
        String dataDateValue = date.getYear() + "-" + (date.getMonthValue() - 1) + "-" + date.getDayOfMonth();
        return By.xpath(
            "//td[not(contains(@class,'p-datepicker-other-month'))]" +
            "//span[@data-date='" + dataDateValue + "']"
        );
    }

    private YearMonth getDisplayedMonthYear() {
        WebElement monthBtn = driver.findElement(MONTH_BUTTON);
        WebElement yearBtn = driver.findElement(YEAR_BUTTON);
        Month month = Month.valueOf(monthBtn.getText().trim().toUpperCase(Locale.ENGLISH));
        int year = Integer.parseInt(yearBtn.getText().trim());
        return YearMonth.of(year, month);
    }

    private void navigateToMonth(YearMonth target) {
        int iterations = 0;
        while (iterations < MAX_MONTH_NAVIGATION_ITERATIONS) {
            YearMonth displayed = getDisplayedMonthYear();
            if (displayed.equals(target)) return;

            By navButton = displayed.isBefore(target) ? NEXT_BUTTON : PREV_BUTTON;
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(navButton));
            btn.click();

            wait.until(d -> !getDisplayedMonthYear().equals(displayed));
            iterations++;
        }
        throw new NoSuchElementException(
            "Could not navigate PrimeNG calendar to target month/year: " + target
        );
    }
}
```

### 2. `CreateCommodityContractPage.java` (relevant excerpt)
```java
package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.PrimeNgCalendarRangeHelper;
import utils.WebDriverCommonLibrary;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateCommodityContractPage {

    private final WebDriver driver;
    private final WebDriverCommonLibrary webDriverCommonLibrary;
    private final PrimeNgCalendarRangeHelper calendarHelper;

    private static final String SECTION_CAPTION = "Product Details";
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CreateCommodityContractPage(WebDriver driver, WebDriverCommonLibrary webDriverCommonLibrary) {
        this.driver = driver;
        this.webDriverCommonLibrary = webDriverCommonLibrary;
        this.calendarHelper = new PrimeNgCalendarRangeHelper(driver, webDriverCommonLibrary);
    }

    public void setTargetDateRange(int rowIndex, LocalDate startDate, LocalDate endDate) {
        By iconLocator = buildDateRangeLocator(rowIndex, "calendaricon[contains(@class,'p-datepicker-icon')]");

        calendarHelper.openCalendar(iconLocator);
        calendarHelper.selectDateRange(startDate, endDate);

        verifyTargetDateRange(rowIndex, startDate, endDate);
    }

    private void verifyTargetDateRange(int rowIndex, LocalDate startDate, LocalDate endDate) {
        By inputLocator = buildDateRangeLocator(rowIndex, "input[@role='combobox']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));

        String actualValue = input.getAttribute("value");
        String expectedValue = startDate.format(INPUT_DATE_FORMAT) + " - " + endDate.format(INPUT_DATE_FORMAT);

        Assert.assertEquals(actualValue, expectedValue,
            "Target Date Range value mismatch for row " + rowIndex +
            ". Expected: " + expectedValue + ", Actual: " + actualValue);
    }

    /**
     * Row/column scoping chain (Steps 1-3):
     *   div[@formarrayname='item_details']  -> stable Angular form-array container
     *   xbs-dcloud-caption[.//h3='Product Details'][rowIndex] -> Nth product-line block
     *   following-sibling::div[1]//table    -> that row's own <table>
     *   count(preceding-sibling::th)+1       -> dynamically resolved column position
     *   xbs-dcloud-date-range                -> confirms correct cell type (safety net)
     */
    private By buildDateRangeLocator(int rowIndex, String leafXPathFragment) {
        String rowScope =
            "//div[@formarrayname='item_details']" +
            "//xbs-dcloud-caption[.//h3[normalize-space()='" + SECTION_CAPTION + "']][" + rowIndex + "]" +
            "/following-sibling::div[1]//table";

        String xpath =
            rowScope +
            "//tbody/tr[1]/td[" +
                "count(" + rowScope + "//thead/tr/th[.//span[normalize-space()='Target Date Range']]/preceding-sibling::th) + 1" +
            "]" +
            "//xbs-dcloud-date-range//" + leafXPathFragment;

        return By.xpath(xpath);
    }
}
```

### 3. `StepsContainer.java` (new step)
```java
import java.time.LocalDate;

@And("user selects target date range from {string} to {string} for row {int} in the {string} section")
public void userSelectsTargetDateRangeForRow(String startDateStr, String endDateStr, int rowIndex, String section) {
    LocalDate startDate = LocalDate.parse(startDateStr);   // ISO format: yyyy-MM-dd
    LocalDate endDate = LocalDate.parse(endDateStr);

    PageObjectManager.getCreateCommodityContractPage().setTargetDateRange(rowIndex, startDate, endDate);
}
```

### 4. `PageObjectManager.java` (add accessor)
```java
private CreateCommodityContractPage createCommodityContractPage;

public CreateCommodityContractPage getCreateCommodityContractPage() {
    if (createCommodityContractPage == null) {
        createCommodityContractPage = new CreateCommodityContractPage(
            DriverFactory.getDriver(), new WebDriverCommonLibrary()
        );
    }
    return createCommodityContractPage;
}
```
(Adjust `WebDriverCommonLibrary` wiring to match your existing singleton pattern if applicable.)

### 5. Example feature file
```gherkin
Feature: Commodity Contract Management - Create

  Scenario: Create a commodity contract with a target date range
    Given user is on the "Create Commodity Contract Management" page
    When user selects target date range from "2026-09-22" to "2026-09-23" for row 1 in the "Product Details" section
    Then the target date range should be saved successfully
```

---

## Design Guarantee Checklist (mapped to original requirements)

| Requirement | How satisfied |
|---|---|
| No hardcoded `td[N]` | `count(preceding-sibling::th)+1` |
| No hardcoded day/month/year in XPath | `data-date` built from `LocalDate`; month/year read dynamically from buttons |
| No `Thread.sleep()` | `WebDriverWait` + `ExpectedConditions` throughout |
| Row-safe (3 separate tables discovered) | `[rowIndex]` predicate on caption + `following-sibling::div[1]` scoping |
| Overlay outside table (`appendTo="body"`) | Icon locator (page-scoped) and popup locators (helper-scoped) always structurally separate |
| Verification reflects real app state | Input `value` attribute — proven necessary since Step 7 showed no start/end distinction in popup CSS classes |
| Reusable/maintainable | `PrimeNgCalendarRangeHelper` has zero CCM-specific knowledge; page object owns only app-specific scoping |
| Java 21 / Selenium 4.47.0 / PrimeNG-Angular compatible | Built and verified against actual captured HTML at every step |

## Known Open Items / Not Yet Implemented
- Disabled-date exclusion class — no evidence captured yet; add only once a real disabled-cell HTML sample is provided
- Month/year button fast-jump sub-view — not used (safe prev/next navigation used instead); could be an optimization if empirically confirmed later
- Symmetrical `ElementClickInterceptedException` JS-click fallback on day-cell clicks — currently only on the icon click; can be added defensively if day-cell click issues are observed in practice
- Popup auto-close behavior after second date click — not confirmed; handle reactively if Step 10 verification intermittently fails due to popup still being open
