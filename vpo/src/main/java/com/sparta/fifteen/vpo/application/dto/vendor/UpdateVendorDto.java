package com.sparta.fifteen.vpo.application.dto.vendor;

import com.sparta.fifteen.vpo.domain.model.VendorType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateVendorDto {

    private UUID vendorId;

    private Long userId;

    private UUID hubId;

    private String vendorName;

    private VendorType vendorType;

    private String vendorAddress;

    public static UpdateVendorDto create(
            UUID vendorId, Long userId, UUID hubId, String vendorName, VendorType vendorType, String vendorAddress) {
        return UpdateVendorDto.builder()
                .vendorId(vendorId)
                .userId(userId)
                .hubId(hubId)
                .vendorName(vendorName)
                .vendorType(vendorType)
                .vendorAddress(vendorAddress)
                .build();
    }
}