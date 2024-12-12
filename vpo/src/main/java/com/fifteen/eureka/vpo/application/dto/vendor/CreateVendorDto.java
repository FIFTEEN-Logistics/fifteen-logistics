package com.fifteen.eureka.vpo.application.dto.vendor;

import com.fifteen.eureka.vpo.domain.model.VendorType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateVendorDto {
    private Long userId;

    private UUID hubId;

    private String vendorName;

    private VendorType vendorType;

    private String vendorAddress;

    public static CreateVendorDto create(
            Long userId, UUID hubId, String vendorName, VendorType vendorType, String vendorAddress) {

        return CreateVendorDto.builder()
                .userId(userId)
                .hubId(hubId)
                .vendorName(vendorName)
                .vendorType(vendorType)
                .vendorAddress(vendorAddress)
                .build();
    }
}
