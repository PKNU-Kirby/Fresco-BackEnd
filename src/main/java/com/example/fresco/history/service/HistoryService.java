package com.example.fresco.history.service;

import com.example.fresco.global.response.paging.PageInfo;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.history.controller.dto.response.HistoryResponse;
import com.example.fresco.history.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public PageResponse<HistoryResponse> getAllHistory(Pageable pageable) {
        Page<HistoryResponse> responsePage = historyRepository.findAll(pageable)
                .map(HistoryResponse::from);

        return new PageResponse<>(
                responsePage.getContent(),
                PageInfo.getPageInfo(responsePage)
        );
    }
}
