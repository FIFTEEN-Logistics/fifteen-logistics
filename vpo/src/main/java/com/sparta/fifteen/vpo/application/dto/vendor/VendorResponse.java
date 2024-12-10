package com.sparta.fifteen.vpo.application.dto.vendor;

import com.sparta.fifteen.vpo.domain.model.Vendor;
import com.sparta.fifteen.vpo.domain.model.VendorType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class VendorResponse {
    private UUID vendorId;
    private UUID hubId;
    private Long userId;
    private String vendorName;
    private String vendorAddress;
    private VendorType vendorType;
    private boolean isDeleted;

    public static VendorResponse of(Vendor vendor) {
        return VendorResponse.builder()
                .vendorId(vendor.getVendorId())
                .hubId(vendor.getHubId())
                .userId(vendor.getUserId())
                .vendorName(vendor.getVendorName())
                .vendorAddress(vendor.getVendorAddress())
                .isDeleted(vendor.isDeleted())
                .build();
    }

}
