package com.fifteen.eureka.vpo.presentation.request.vendor;

import com.fifteen.eureka.vpo.application.dto.vendor.CreateVendorDto;
import com.fifteen.eureka.vpo.domain.model.VendorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateVendorRequest {

    @NotNull(message = "담당자 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "허브 ID는 필수입니다.")
    private UUID hubId;

    @NotBlank(message = "업체명은 필수입니다.")
    @Size(max = 50, message = "업체명은 최대 50자까지 입력할 수 있습니다.")
    private String vendorName;

    @NotNull(message = "업체 타입은 필수입니다.")
    private VendorType vendorType;

    @NotBlank(message = "업체 주소는 필수입니다.")
    @Size(max = 500, message = "업체 주소는 최대 500자까지 입력할 수 있습니다.")
    private String vendorAddress;

    public CreateVendorDto toDto() {
        return CreateVendorDto.create(this.userId, this.hubId, this.vendorName, this.vendorType, this.vendorAddress);
    }

}
