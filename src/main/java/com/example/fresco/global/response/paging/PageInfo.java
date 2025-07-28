package com.example.fresco.global.response.paging;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class PageInfo {
    private int currentPage;        // 현재 페이지 (1부터 시작하게 변환)
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;

    public static PageInfo getPageInfo(Page<?> page) {
        return PageInfo.builder()
                .currentPage(page.getNumber() + 1)  // 0-based를 1-based로 변환
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }
}
