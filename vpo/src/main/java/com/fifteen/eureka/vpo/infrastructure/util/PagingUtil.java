package com.fifteen.eureka.vpo.infrastructure.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingUtil {
    public static Pageable createPageable(int page, int size, boolean isAsc, String sortBy) {
        size = (size == 10 || size == 30 || size == 50) ? size : 10;
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy.equals("updatedAt") ? "updatedAt" : "createdAt");
        return PageRequest.of(page, size, sort);
    }
}
