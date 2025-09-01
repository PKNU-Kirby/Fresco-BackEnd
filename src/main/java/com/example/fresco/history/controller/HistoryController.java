package com.example.fresco.history.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.global.response.success.HistorySuccessCode;
import com.example.fresco.history.controller.dto.response.HistoryResponse;
import com.example.fresco.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping
    public SuccessResponse<PageResponse<HistoryResponse>> getAllHistoryResponse(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return SuccessResponse.of(HistorySuccessCode.GET_HISTORY_SUCCESS,
                historyService.getAllHistory(pageable));
    }
}