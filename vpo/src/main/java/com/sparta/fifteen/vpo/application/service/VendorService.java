package com.sparta.fifteen.vpo.application.service;

import com.sparta.fifteen.vpo.application.dto.vendor.CreateVendorDto;
import com.sparta.fifteen.vpo.application.dto.vendor.UpdateVendorDto;
import com.sparta.fifteen.vpo.application.dto.vendor.VendorResponse;
import com.sparta.fifteen.vpo.domain.model.Vendor;
import com.sparta.fifteen.vpo.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorResponse createVendor(CreateVendorDto request) {

        Vendor vendor = Vendor.create(
                request.getHubId(),
                request.getUserId(),
                request.getVendorName(),
                request.getVendorType(),
                request.getVendorAddress()
        );

        vendorRepository.save(vendor);

        return VendorResponse.of(vendor);
    }

    public Page<VendorResponse> getVendors(Pageable pageable) {
        Page<Vendor> vendors = vendorRepository.findAll(pageable);
        List<VendorResponse> contents = vendors.getContent().stream().map(VendorResponse::of).toList();
        return new PageImpl<>(contents, pageable, vendors.getSize());
    }

    public VendorResponse getVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow();
        return VendorResponse.of(vendor);
    }

    @Transactional
    public VendorResponse updateVendor(UUID vendorId,UpdateVendorDto request) {

        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow();

        vendor.update(
                request.getHubId(),
                request.getUserId(),
                request.getVendorName(),
                request.getVendorType(),
                request.getVendorAddress()
        );

        return VendorResponse.of(vendor);

    }

    @Transactional
    public VendorResponse deleteVendor(UUID vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow();

        vendor.delete();

        return VendorResponse.of(vendor);
    }
}
