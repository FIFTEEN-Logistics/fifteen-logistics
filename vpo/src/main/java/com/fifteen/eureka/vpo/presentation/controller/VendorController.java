package com.fifteen.eureka.vpo.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.vpo.application.dto.vendor.VendorResponse;
import com.fifteen.eureka.vpo.application.service.VendorService;
import com.fifteen.eureka.vpo.presentation.request.vendor.CreateVendorRequest;
import com.fifteen.eureka.vpo.presentation.request.vendor.UpdateVendorRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ApiResponse<?> createVendor(@Valid @RequestBody CreateVendorRequest request) {
        return ApiResponse.OK(ResSuccessCode.CREATED, vendorService.createVendor(request.toDto()));
    }

    @GetMapping
    public ApiResponse<Page<VendorResponse>> getVendors(@PageableDefault(
            sort = "createdBy") Pageable pageable) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, vendorService.getVendors(pageable));
    }

    @GetMapping("/{vendorId}")
    public ApiResponse<?> getVendor(@PathVariable UUID vendorId) {
        return ApiResponse.OK(ResSuccessCode.SUCCESS, vendorService.getVendor(vendorId));
    }

    @PutMapping("/{vendorId}")
    public ApiResponse<?> updateVendor(
            @PathVariable UUID vendorId,
            @Valid @RequestBody UpdateVendorRequest request) {

        return ApiResponse.OK(ResSuccessCode.UPDATED, vendorService.updateVendor(vendorId, request.toDto()));
    }

    @DeleteMapping("/{vendorId}")
    public ApiResponse<?> deleteVendor(@PathVariable UUID vendorId) {
        return ApiResponse.OK(ResSuccessCode.DELETED, vendorService.deleteVendor(vendorId));
    }

}
