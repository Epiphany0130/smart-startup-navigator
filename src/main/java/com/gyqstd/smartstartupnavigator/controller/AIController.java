package com.gyqstd.smartstartupnavigator.controller;

import com.gyqstd.smartstartupnavigator.SmartStartupNavigatorApplication;
import com.gyqstd.smartstartupnavigator.app.SmartStartupService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.tools.Tool;

/**
 * @author GuYuqi
 * @version 1.0
 */
@RestController
@RequestMapping("/ai")
public class AIController {

    @Resource
    private SmartStartupService smartStartupService;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 同步调用 智能创业导航服务
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/smart_startup/chat/sync")
    public String doChatWithSmartStartupSync(String message, String chatId) {
        return smartStartupService.doChat(SmartStartupService.ServiceType.TOTAL ,message, chatId);
    }

    /**
     * SSE 流式调用 智能创业导航服务 - 创意阶段
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_startup/chat/sse/ecreat_stage", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartStartupSSECreatStage(String message, String chatId) {
        return smartStartupService.doChatByStream(SmartStartupService.ServiceType.CREATIVE_STAGE ,message, chatId);
    }

    /**
     * SSE 流式调用 智能创业导航服务 - 创业类型
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_startup/chat/sse/entrepreneurship_types", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartStartupSSEEntrepreneurshipTypes(String message, String chatId) {
        return smartStartupService.doChatByStream(SmartStartupService.ServiceType.ENTREPRENEURSHIP_TYPES ,message, chatId);
    }

    /**
     * SSE 流式调用 智能创业导航服务 - 商业计划
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_startup/chat/sse/business_plan", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartStartupSSEBusinessPlan(String message, String chatId) {
        return smartStartupService.doChatByStream(SmartStartupService.ServiceType.BUSINESS_PLAN ,message, chatId);
    }

    /**
     * SSE 流式调用 智能创业导航服务 - 品牌与文案
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_startup/chat/sse/brand_and_copywriting", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartStartupSSEBrandAndCopywriting(String message, String chatId) {
        return smartStartupService.doChatByStream(SmartStartupService.ServiceType.BRAND_AND_COPYWRITING ,message, chatId);
    }

    /**
     * SSE 流式调用 智能创业导航服务 - 市场分析
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_startup/chat/sse/market_analysis", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartStartupSSEMarketAnalysis(String message, String chatId) {
        return smartStartupService.doChatByStream(SmartStartupService.ServiceType.MARKET_ANALYSIS ,message, chatId);
    }

    /**
     * SSE 流式调用 智能创业导航服务 - 注册筹备
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_startup/chat/sse/registration_preparation", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartStartupSSERegistrationPreparation(String message, String chatId) {
        return smartStartupService.doChatByStream(SmartStartupService.ServiceType.REGISTRATION_PREPARATION ,message, chatId);
    }

    /**
     * SSE 流式调用 智能创业导航服务 - 公司成立
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_startup/chat/sse/company_establishment", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartStartupSSECompanyEstablishment(String message, String chatId) {
        return smartStartupService.doChatByStream(SmartStartupService.ServiceType.COMPANY_ESTABLISHMENT ,message, chatId);
    }


}
