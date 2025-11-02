package com.gyqstd.smartstartupnavigator.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author GuYuqi
 * @version 1.0
 */
@SpringBootTest
public class PDFGenerationToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool toolPDF = new PDFGenerationTool();
        WebSearchTool toolWebSearch = new WebSearchTool(searchApiKey);
        String fileName = "PDF 测试";
        String query = "我要开一家公司，你有什么建议？";
        String resultOfSearch = toolWebSearch.searchWeb(query);
        String result = toolPDF.generatePDF(fileName, resultOfSearch);
        assertNotNull(result);
    }
}
