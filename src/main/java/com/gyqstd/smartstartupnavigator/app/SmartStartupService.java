package com.gyqstd.smartstartupnavigator.app;

import com.gyqstd.smartstartupnavigator.advisor.MyLoggerAdvisor;
import com.gyqstd.smartstartupnavigator.constant.Prompt;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;


/**
 * 智能创业导航服务
 * 合并了原来的多个单独服务类
 */
@Component
@Slf4j
public class SmartStartupService {

    /**
     * 服务类型枚举
     */
    public enum ServiceType {
        TOTAL(Prompt.TOTAL_SYSTEM_PROMPT),
        ENTREPRENEURSHIP_TYPES(Prompt.ENTREPRENEURSHIP_TYPES_SYSTEM_PROMPT),
        CREATIVE_STAGE(Prompt.CREATIVE_STAGE_SYSTEM_PROMPT),
        BUSINESS_PLAN(Prompt.BUSINESS_PLAN_SYSTEM_PROMPT),
        MARKET_ANALYSIS(Prompt.MARKET_ANALYSIS_SYSTEM_PROMPT),
        BRAND_AND_COPYWRITING(Prompt.BRAND_AND_COPYWRITING_SYSTEM_PROMPT),
        REGISTRATION_PREPARATION(Prompt.REGISTRATION_PREPARATION_SYSTEM_PROMPT),
        COMPANY_ESTABLISHMENT(Prompt.COMPANY_ESTABLISHMENT_SYSTEM_PROMPT);

        private final String systemPrompt;

        ServiceType(String systemPrompt) {
            this.systemPrompt = systemPrompt;
        }

        public String getSystemPrompt() {
            return systemPrompt;
        }
    }

    private final ChatModel dashscopeChatModel;
    private final ChatClient totalChatClient;
    private final ChatClient entrepreneurshipTypesChatClient;
    private final ChatClient creativeStageChatClient;
    private final ChatClient businessPlanChatClient;
    private final ChatClient marketAnalysisChatClient;
    private final ChatClient brandAndCopywritingChatClient;
    private final ChatClient registrationPreparationChatClient;
    private final ChatClient companyEstablishmentChatClient;

    public SmartStartupService(ChatModel dashscopeChatModel) {
        this.dashscopeChatModel = dashscopeChatModel;
        
        // 初始化各个服务的 ChatClient
        this.totalChatClient = createChatClient(ServiceType.TOTAL);
        this.entrepreneurshipTypesChatClient = createChatClient(ServiceType.ENTREPRENEURSHIP_TYPES);
        this.creativeStageChatClient = createChatClient(ServiceType.CREATIVE_STAGE);
        this.businessPlanChatClient = createChatClient(ServiceType.BUSINESS_PLAN);
        this.marketAnalysisChatClient = createChatClient(ServiceType.MARKET_ANALYSIS);
        this.brandAndCopywritingChatClient = createChatClient(ServiceType.BRAND_AND_COPYWRITING);
        this.registrationPreparationChatClient = createChatClient(ServiceType.REGISTRATION_PREPARATION);
        this.companyEstablishmentChatClient = createChatClient(ServiceType.COMPANY_ESTABLISHMENT);
    }

    /**
     * 创建 ChatClient
     * @param serviceType 服务类型
     * @return ChatClient 实例
     */
    private ChatClient createChatClient(ServiceType serviceType) {
        // 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();

        return ChatClient.builder(dashscopeChatModel)
                .defaultSystem(serviceType.getSystemPrompt())
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    /**
     * 根据服务类型获取对应的 ChatClient
     * @param serviceType 服务类型
     * @return 对应的 ChatClient
     */
    private ChatClient getChatClient(ServiceType serviceType) {
        return switch (serviceType) {
            case TOTAL -> totalChatClient;
            case ENTREPRENEURSHIP_TYPES -> entrepreneurshipTypesChatClient;
            case CREATIVE_STAGE -> creativeStageChatClient;
            case BUSINESS_PLAN -> businessPlanChatClient;
            case MARKET_ANALYSIS -> marketAnalysisChatClient;
            case BRAND_AND_COPYWRITING -> brandAndCopywritingChatClient;
            case REGISTRATION_PREPARATION -> registrationPreparationChatClient;
            case COMPANY_ESTABLISHMENT -> companyEstablishmentChatClient;
        };
    }

    // tools
    @Resource
    private ToolCallback[] allTools;

    // mcp
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * 对话方法
     * @param serviceType 服务类型
     * @param message 用户消息
     * @param chatId 对话ID
     * @return AI 回复内容
     */
    public String doChat(ServiceType serviceType, String message, String chatId) {
        ChatClient chatClient = getChatClient(serviceType);

        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) // 指定对话 ID
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)) // 指定记忆大小
                .advisors(new MyLoggerAdvisor()) // 添加日志记录 Advisor
                .tools(toolCallbackProvider) // mcp 工具回调
                .tools(allTools) // 注册工具回调
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 流式调用对话方法
     * @param serviceType
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(ServiceType serviceType, String message, String chatId) {
            ChatClient chatClient = getChatClient(serviceType);

            return chatClient
                    .prompt()
                    .user(message)
                    .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) // 指定对话 ID
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)) // 指定记忆大小
                    .advisors(new MyLoggerAdvisor()) // 添加日志记录 Advisor
                    .tools(toolCallbackProvider) // mcp 工具回调
                .tools(allTools) // 注册工具回调
                .stream()
                .content();
    }



}