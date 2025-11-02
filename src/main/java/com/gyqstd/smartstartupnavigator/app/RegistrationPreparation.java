package com.gyqstd.smartstartupnavigator.app;

import com.gyqstd.smartstartupnavigator.advisor.MyLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static com.gyqstd.smartstartupnavigator.constant.Prompt.MARKET_ANALYSIS_SYSTEM_PROMPT;
import static com.gyqstd.smartstartupnavigator.constant.Prompt.REGISTRATION_PREPARATION_SYSTEM_PROMPT;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class RegistrationPreparation {

    // 注入 ChatClient，支持多轮对话
    private final ChatClient chatClient;

    public RegistrationPreparation(ChatModel dashscopeChatModel) {
        // 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();

        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(REGISTRATION_PREPARATION_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
//                        ,new ReReadingAdvisor()
                )
                .build();
    }

    // 对话方法
    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) // 指定对话 ID
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)) // 指定记忆大小
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}