package com.fifteen.eureka.vpo.presentation.request.vendor;

import com.fifteen.eureka.vpo.application.dto.vendor.CreateVendorDto;
import com.fifteen.eureka.vpo.domain.model.VendorType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateVendorRequest {

    private Long userId;

    private UUID hubId;

    private String vendorName;

    private VendorType vendorType;

    private String vendorAddress;

    public CreateVendorDto toDto() {
        return CreateVendorDto.create(this.userId, this.hubId, this.vendorName, this.vendorType, this.vendorAddress);
    }

}
