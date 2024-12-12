package com.fifteen.eureka.vpo.presentation.request.vendor;

import com.fifteen.eureka.vpo.application.dto.vendor.UpdateVendorDto;
import com.fifteen.eureka.vpo.domain.model.VendorType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateVendorRequest {

    private Long userId;

    private UUID hubId;

    private String vendorName;

    private VendorType vendorType;

    private String vendorAddress;

    public UpdateVendorDto toDto() {
        return UpdateVendorDto.create(this.userId, this.hubId, this.vendorName, this.vendorType, this.vendorAddress);
    }
}
