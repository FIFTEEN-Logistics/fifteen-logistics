package com.sparta.fifteen.vpo.presentation.request.vendor;

import com.sparta.fifteen.vpo.application.dto.vendor.CreateVendorDto;
import com.sparta.fifteen.vpo.domain.model.VendorType;
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
