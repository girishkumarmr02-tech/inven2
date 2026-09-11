package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import invensoft.testdata.ContractTestData;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestDataReader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ContractTestData getByTestCaseId(String testCaseId) throws IOException {
        String filePath = "src/test/resources/testdata/contractTestData.json";

        Map<String, List<ContractTestData>> wrapper = mapper.readValue(new File(filePath), new TypeReference<Map<String, List<ContractTestData>>>() {});

        return wrapper.get("contractTestData").stream()
                .filter(td -> td.getTestCaseId().equals(testCaseId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No test data found for testCaseId: " + testCaseId));
    }
}