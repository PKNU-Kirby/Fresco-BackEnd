package com.example.fresco.ingredient.service.util.dataClient;

import com.example.fresco.ingredient.controller.dto.response.IngredientListResponse;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptMatchListResponse;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class DataApiClient {

    private final WebClient web;

    public DataApiClient() {
        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(ssl -> ssl.sslContext(sslContext));

            this.web = WebClient.builder()
                    .baseUrl("https://data.fresco.kro.kr")
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("WebClient 초기화 실패", e);
        }
    }

    public ReceiptMatchListResponse sendReceipt(List<String> foodNames) {
        return web.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/search");
                    foodNames.forEach(foodName ->
                            uriBuilder.queryParam("ingredient_names", foodName)
                    );
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ReceiptMatchListResponse.ReceiptMatchResponse>>() {
                })
                .timeout(Duration.ofMinutes(2))
                .map(ReceiptMatchListResponse::new)
                .block();
    }

    public IngredientListResponse sendImage(MultipartFile ingredientImage) {
        try {
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("image", ingredientImage.getResource());

            return web.post()
                    .uri("/detect")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<IngredientResponse>>() {
                    })
                    .map(IngredientListResponse::new)
                    .timeout(Duration.ofSeconds(30))
                    .onErrorReturn(new IngredientListResponse(Collections.emptyList()))
                    .block();

        } catch (Exception e) {
            log.error("이미지 API 실패: ", e);
            return new IngredientListResponse(Collections.emptyList());
        }
    }
}