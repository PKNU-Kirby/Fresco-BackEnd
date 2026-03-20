package com.example.fresco.history.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.RefrigeratorErrorCode;
import com.example.fresco.global.response.paging.PageInfo;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.history.controller.dto.response.HistoryResponse;
import com.example.fresco.history.domain.repository.HistoryRepository;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ssm.model.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public PageResponse<HistoryResponse> getAllHistory(Long refrigeratorId, Pageable pageable) {
        Page<HistoryResponse> responsePage = historyRepository.findAllByRefrigeratorId(refrigeratorId, pageable)
                .map(HistoryResponse::from);

        return new PageResponse<>(
                responsePage.getContent(),
                PageInfo.getPageInfo(responsePage)
        );
    }
}
