package com.sparta.fifteen.vpo.presentation.controller;

import com.sparta.fifteen.vpo.application.dto.vendor.VendorResponse;
import com.sparta.fifteen.vpo.application.service.VendorService;
import com.sparta.fifteen.vpo.presentation.request.vendor.UpdateVendorRequest;
import com.sparta.fifteen.vpo.presentation.request.vendor.CreateVendorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<?> createVendor(@RequestBody CreateVendorRequest request) {
        return ResponseEntity.ok(vendorService.createVendor(request.toDto()));
    }

    @GetMapping
    public ResponseEntity<Page<VendorResponse>> getVendors(@PageableDefault(
            sort = "vendorName") Pageable pageable) {
        return ResponseEntity.ok(vendorService.getVendors(pageable));
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<?> getVendor(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(vendorService.getVendor(vendorId));
    }

    @PutMapping("/{vendorId}")
    public ResponseEntity<?> updateVendor(
            @PathVariable UUID vendorId,
            @RequestBody UpdateVendorRequest request) {

        return ResponseEntity.ok(vendorService.updateVendor(vendorId, request.toDto()));
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<?> deleteVendor(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(vendorService.deleteVendor(vendorId));
    }

}
