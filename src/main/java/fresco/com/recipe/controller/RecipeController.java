package fresco.com.recipe.controller;

import fresco.com.recipe.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService openAiService;

    public RecipeController(RecipeService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return openAiService.chat(prompt);
    }
}
