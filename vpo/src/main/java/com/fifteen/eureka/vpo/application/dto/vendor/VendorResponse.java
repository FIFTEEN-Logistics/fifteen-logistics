package com.fifteen.eureka.vpo.application.dto.vendor;

import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.model.VendorType;
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
    private Long hubManagerId;
    private String vendorName;
    private String vendorAddress;
    private VendorType vendorType;

    public static VendorResponse of(Vendor vendor) {
        return VendorResponse.builder()
                .vendorId(vendor.getVendorId())
                .hubId(vendor.getHubId())
                .userId(vendor.getUserId())
                .hubManagerId(vendor.getHubManagerId())
                .vendorName(vendor.getVendorName())
                .vendorAddress(vendor.getVendorAddress())
                .vendorType(vendor.getVendorType())
                .build();
    }

}
