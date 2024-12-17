package com.fifteen.eureka.vpo.domain.model;

import lombok.Getter;

@Getter
public enum VendorType {

    SUPPLIER("공급업체"), RECEIVER("수령업체");

    private final String label;

    VendorType(String label) {
        this.label = label;
    }
}
