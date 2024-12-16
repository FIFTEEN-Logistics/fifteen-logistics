package com.fifteen.eureka.vpo.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.RoleCheck;
import com.fifteen.eureka.vpo.application.dto.vendor.VendorResponse;
import com.fifteen.eureka.vpo.application.service.VendorService;
import com.fifteen.eureka.vpo.infrastructure.util.PagingUtil;
import com.fifteen.eureka.vpo.presentation.request.vendor.CreateVendorRequest;
import com.fifteen.eureka.vpo.presentation.request.vendor.UpdateVendorRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB"})
    @PostMapping
    public ApiResponse<?> createVendor(@Valid @RequestBody CreateVendorRequest request) {
        return ApiResponse.OK(ResSuccessCode.CREATED, vendorService.createVendor(request.toDto()));
    }

    @GetMapping
    public ApiResponse<Page<VendorResponse>> getVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
        return ApiResponse.OK(ResSuccessCode.SUCCESS, vendorService.getVendors(pageable, keyword));
    }

    @GetMapping("/{vendorId}")
    public ApiResponse<?> getVendor(@PathVariable UUID vendorId) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, vendorService.getVendor(vendorId));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB", "ROLE_ADMIN_VENDOR"})
    @PutMapping("/{vendorId}")
    public ApiResponse<?> updateVendor(
            @PathVariable UUID vendorId,
            @Valid @RequestBody UpdateVendorRequest request) {

        return ApiResponse.OK(ResSuccessCode.UPDATED, vendorService.updateVendor(vendorId, request.toDto()));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER", "ROLE_DELIVERY_HUB"})
    @DeleteMapping("/{vendorId}")
    public ApiResponse<?> deleteVendor(@PathVariable UUID vendorId) {
        return ApiResponse.OK(ResSuccessCode.DELETED, vendorService.deleteVendor(vendorId));
    }

}
