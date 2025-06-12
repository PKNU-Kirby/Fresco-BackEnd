package fresco.com.recipe.dto;

import java.util.List;

public class OpenAiResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String content;
    }
}
