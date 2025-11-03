package com.gyqstd.smartstartupnavigator.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
/**
 *  @author GuYuqi
 *  @version 1.0
 */
@SpringBootTest
class SmartStartupServiceTest {

    @Autowired
    private SmartStartupService smartStartupService;

    @Test
    void doChat() {
        // 测试联网搜索问题的答案
        testMessage("我要开一家汉服定制的公司，你有什么建议吗");

        // 测试 PDF 生成
        testMessage("生成一份‘汉服定制公司创业计划’PDF，类似于商业计划书的形式");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = smartStartupService.doChat(SmartStartupService.ServiceType.TOTAL, message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "我要开一家汉服定制公司，你帮我找潍坊经济开发区民主街7777号潍坊理工学院(中心校区)附近适合公司落地的地点，请你调用高德地图搜索";
        String answer = smartStartupService.doChat(SmartStartupService.ServiceType.COMPANY_ESTABLISHMENT, message, chatId);
        Assertions.assertNotNull(answer);
    }

}