package com.example.fresco.recipe.util;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final OpenAiChatModel model;

    public String callFunction(String prompt) {
        ChatClient chat = ChatClient.builder(model).build();
        String content = chat
                .prompt()
                .messages(
                        new SystemMessage("아래 JSON 형식을 반드시 지켜서 반환하라. 레시피에 사용된 모든 재료를 표시하고, " +
                                "steps의 각 요소 앞에는 \"1. \",\"2. \" 이런식으로 숫자를 붙여 나오게 하라." +
                                ":\n{ title: string, ingredients: [{ingredientName: [string], quantity: [number], unit: [string]}], steps: [string], substitutions: [{original: [string], substitute: [string]}] }"),
                        new AssistantMessage("""
                                {
                                  "title":"예시 볶음밥",
                                  "ingredients":[{"ingredientName":"당근","quantity":"2.0","unit":"개"}],
                                  "steps":["1. 팬에 기름을 두른다 ..."],
                                  "substitutions":[{"original":"당근","substitute":"애호박"}]
                                }
                                """),
                        new UserMessage(prompt)
                )
                .call()
                .content();
        if (content == null || content.isBlank()) {
            throw new IllegalStateException("OpenAI 응답이 비어 있습니다.");
        }

        return content;
    }
}
