package invensoft.testdata;


import java.util.List;

public class ContractTestData {
    private String testCaseId;
    private List<ProductDetailRow> productDetails;

    public String getTestCaseId() {
        return testCaseId;
    }
    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }
    public List<ProductDetailRow> getProductDetails() {
        return productDetails;
    }
    public void setProductDetails(List<ProductDetailRow> productDetails) {
        this.productDetails = productDetails;
    }
}
