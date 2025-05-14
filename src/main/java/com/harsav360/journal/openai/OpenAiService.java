package com.harsav360.journal.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeJournals(String prompt) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    ),
                    "temperature", 0.7
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode jsonNode = objectMapper.readTree(response.body());
            return jsonNode
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error analyzing journal entries.";
        }
    }
}
