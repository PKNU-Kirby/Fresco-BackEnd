package com.example.fresco.ingredient.service.util.ocr;

import com.example.fresco.ingredient.controller.dto.response.IngredientMatchInfo;
import com.example.fresco.ingredient.controller.dto.response.ReceiptMatchResponse;
import com.example.fresco.ingredient.controller.dto.response.ocr.FoodPair;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.util.List;

@Component
public class ReceiptApiClient {

    private final WebClient web;

    public ReceiptApiClient() {
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

    public ReceiptMatchResponse sendReceipt(List<String> foodNames) {
        return web.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/search/");
                    foodNames.forEach(foodName ->
                            uriBuilder.queryParam("product_names", foodName)
                    );
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IngredientMatchInfo>>() {
                })
                .map(ReceiptMatchResponse::new)
                .block();
    }
}