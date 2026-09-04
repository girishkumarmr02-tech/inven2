
@XBS_Commodity_Contract_Management
Feature: Commodity Contract Management
  As a Contract Manager.I want to create a Commodity Contract with only the mandatory fields
  So that I can quickly onboard a new supplier agreement without unnecessary data entry.

  @XBS_Commodity_Contract_Management_Flow @smoke @regression
  Scenario: Create Commodity Contract Management end to end flow with only mandatory fields

    ## Login
    Given open browser
    When enter the user name as "username"
    When enter the user password as "password"
    Then the user should be navigated to the dashboard

    ## Dashboard Verification
#    And verify "Purchase" section is displayed on the dashboard page
#    And verify "Sales" section is displayed on the dashboard page
#    And verify "Inventory" section is displayed on the dashboard page
#    And verify "WI" section is displayed on the dashboard page
#    And verify "Product Summary" section is displayed on the dashboard page
#
#    ## Dashboard sidebar menu
#    Then verify "Commercial" is displayed in the dashboard sidebar menu
#    Then verify "Logistics" is displayed in the dashboard sidebar menu
#    Then verify "Quality" is displayed in the dashboard sidebar menu
#    Then verify "Finance" is displayed in the dashboard sidebar menu
#    Then verify "Production" is displayed in the dashboard sidebar menu
#    Then verify "Setup" is displayed in the dashboard sidebar menu
#    Then verify "Admin" is displayed in the dashboard sidebar menu
#    Then verify "Futures" is displayed in the dashboard sidebar menu
#    Then verify "reports" is displayed in the dashboard sidebar menu

#    ## Navigate to Module
    When user navigates to "Commercial" dashboard sidebar menu
#    And verify "Commodity Contract Management" section is displayed on the dashboard page
    When user clicks on "Commodity Contract Management" section on the dashboard page
#
#    ## Verify buttons from commodity contract management dashboard
#    Then verify "Create""plus" prime icons is displayed on the Commercial Commodity Contract Management dashboard
#    Then verify "Copy By Template""copy" prime icons is displayed on the Commercial Commodity Contract Management dashboard
#    Then verify "Rolling""calendar" prime icons is displayed on the Commercial Commodity Contract Management dashboard
#    Then verify "Force Close""times" prime icons is displayed on the Commercial Commodity Contract Management dashboard
#    Then verify "Toggle Columns""bi bi-list-check" boot strap icon is displayed on the Commercial Commodity Contract Management dashboard
#    Then verify "Filter""filter" prime icons is displayed on the Commercial Commodity Contract Management dashboard
#    Then verify "More""angle-down" prime icons is displayed on the Commercial Commodity Contract Management dashboard

#    ## Commodity Contract Management
    When clicks on "Create" "plus" icon on the Commodity Contract Management dashboard
#    Then verifies that the "Commodity Contract Management - Create General" popup is displayed
#
#    And verifies that the "General" section is displayed
#    And verifies that the "Product Details" table section is displayed
#    And verifies that the "Commission Details" table section is displayed
#    And verifies that the "Delivery Terms" section is displayed
#    And verifies that the "Documents" section is displayed
#    And verifies that the "Remarks and Instructions" section is displayed
#
#    When user selects "Coffee Farmer Purchase Contract" from the "Class" dropdown in the "General" section
#    When user selects "EUR" from the "Invoice Currency" dropdown in the "General" section
#    When user selects "INR/Kg" from the "Price Currency / UOM" dropdown in the "General" section
#    When user selects "IMMEDIATE" from the "Payment Terms" dropdown in the "General" section
#
#
#    When user selects "FLAT PRICE" from the "Pricing" dropdown in the "General" section
#    When user selects "Transporter (busan)" from the "Counterparty" dropdown in the "General" section
#    When user selects "Default (DND)" from the "Billing Location" dropdown in the "General" section
#    When user selects "Default (DND)" from the "Shipping Location" dropdown in the "General" section
#    When user selects "Transporter (busan)" from the "Shipper" dropdown in the "General" section
#    When user selects "GST 0%" from the "Tax Structure" dropdown in the "General" section
#
    #Look-up
##  When user selects "Girish" from the "Trader" dropdown in the "General" section
#    When user selects "Season 2026 - 2027" from the "Season" dropdown in the "General" section
#    When user selects "Prior to 1st Notice Day" from the "Fixation Terms" dropdown in the "General" section
#    When user selects "NEW YORK - ARABICA" from the "Exchange" dropdown in the "General" section

    ##Picklist--fix it later not Mandatory
    ##When user selects "BUYER'S CALL" from the "Fixation By" dropdown in the "General" section

    ###Verify product details section
#    And verifies that the "Product" column is displayed in the "Product Details" section table
#    And verifies that the "Units" column is displayed in the "Product Details" section table
#    And verifies that the "Packing" column is displayed in the "Product Details" section table
#    And verifies that the "Qty." column is displayed in the "Product Details" section table
#    And verifies that the "UOM" column is displayed in the "Product Details" section table
#    And verifies that the "No Of Lots." column is displayed in the "Product Details" section table
#    And verifies that the "Price" column is displayed in the "Product Details" section table
#    And verifies that the "Target Type" column is displayed in the "Product Details" section table
#    And verifies that the "Units" column is displayed in the "Product Details" section table
#    And verifies that the "Target Date Range" column is displayed in the "Product Details" section table
#    And verifies that the "Trade Month" column is displayed in the "Product Details" section table
#    And verifies that the "Position Month" column is displayed in the "Product Details" section table
#    And verifies that the "Quality" column is displayed in the "Product Details" section table
#    And verifies that the "Certification & Premium" column is displayed in the "Product Details" section table
#    And verifies that the "Action" column is displayed in the "Product Details" section table

#    ###Adding data in Product Terms section
#    When user selects "AC A" from the "Product" dropdown in the "Product Details" table
#    When user enters "600" in the "Units" number field in the "Product Details" table
#    When user selects "1 MT Big Bags" from the "Packing" dropdown type in the "Product Details" table
#    When user enters "600" in the "Qty." number field in the "Product Details" table
#    When user enters "600" in the "Price" number field in the "Product Details" table

    ################***********************need work not stable *****************##############
#    When user selects "Shipment" from the "Target Type" dropdown type in the "Product Details" table
#     When user selects target date range from "2026-09-16" to "2026-09-20" in the "Target Date Range" field in the "Product Details" table
     When test selects target date range from "2026-09-16" to "2026-09-20" in the "Target Date Range" field in the "Product Details" table
#
#    #Implement later exchange scenario not applicable for flat price
#    ##When user selects "" from the "Trade Month" dropdown in the "Product Details" table
#
#    ##Verify Commission Details section
###    And verifies that the "Commission Agent / Broker" column is displayed in the "Commission Details" section table
###    And verifies that the "Ref. No." column is displayed in the "Commission Details" section table
###    And verifies that the "Type" column is displayed in the "Commission Details" section table
###    And verifies that the "Value" column is displayed in the "Commission Details" section table
#      ###And verifies that the "Action" column is displayed in the "Commission Details" section table
#      When user selects "Agent" from the "Commission Agent / Broker" dropdown in the "Commission Details" table
#
#
#    ###Adding data in Delivery Terms section
#    When user selects "DDP" from the "INCO Terms" dropdown in the "Delivery Terms" section
#    When user selects "Re Weights" from the "Weight Terms" dropdown in the "Delivery Terms" section
#    When user enters "Mysuru" in "Place" text field in the "Delivery Terms" section
#    When user selects "AB" from the "Abritration" dropdown in the "Delivery Terms" section
#
#    When user selects "CERTIFICATES REQUIRED FOR ECC" from the "Document Set" dropdown in the "Documents" section
#
#    When user enters "Delivery should meet the agreed quality specifications." in "Remarks" long text area field in the "Remarks and Instructions" section
#    When user enters "Price confirmed with supplier; pending manager approval." in "Instructions" long text area field in the "Remarks and Instructions" section
#    When user enters "Pack in 1 MT Big Bags and deliver to the designated warehouse." in "Internal Remarks" long text area field in the "Remarks and Instructions" section

    And click on "Save" button
