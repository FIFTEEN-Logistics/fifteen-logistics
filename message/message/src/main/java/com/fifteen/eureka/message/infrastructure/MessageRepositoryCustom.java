package com.fifteen.eureka.message.infrastructure;

import com.fifteen.eureka.message.presentation.dtos.response.MessageGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositoryCustom {

    Page<MessageGetResponse> findAllWithSearch(Pageable pageable, String search);
}
