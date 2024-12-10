package com.sparta.fifteen.vpo.presentation.request.vendor;

import com.sparta.fifteen.vpo.application.dto.vendor.UpdateVendorDto;
import com.sparta.fifteen.vpo.domain.model.VendorType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateVendorRequest {

    private UUID vendorId;

    private Long userId;

    private UUID hubId;

    private String vendorName;

    private VendorType vendorType;

    private String vendorAddress;

    public UpdateVendorDto toDto() {
        return UpdateVendorDto.create(this.vendorId, this.userId, this.hubId, this.vendorName, this.vendorType, this.vendorAddress);
    }
}
