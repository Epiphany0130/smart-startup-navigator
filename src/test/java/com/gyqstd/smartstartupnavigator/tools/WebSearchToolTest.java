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
public class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    public void testSearchWeb() {
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "我要开一家公司，你有什么建议？";
        String result = tool.searchWeb(query);
        assertNotNull(result);
    }
}
