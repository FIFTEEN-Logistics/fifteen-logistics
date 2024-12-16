package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.vpo.application.dto.vendor.CreateVendorDto;
import com.fifteen.eureka.vpo.application.dto.vendor.UpdateVendorDto;
import com.fifteen.eureka.vpo.application.dto.vendor.VendorResponse;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.infrastructure.client.HubClient;
import com.fifteen.eureka.vpo.infrastructure.client.UserClient;
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
    private final HubClient hubClient;
    private final UserClient userClient;

    @Transactional
    public VendorResponse createVendor(CreateVendorDto request) {

//        if(!hubClient.getHub(request.getHubId()).getCode().equals(20000)) {
//            throw new CustomApiException(ResErrorCode.NOT_FOUND);
//        }
//
//        if(!userClient.getUser(request.getUserId()).getCode().equals(20000)) {
//            throw new CustomApiException(ResErrorCode.NOT_FOUND);
//        }

        //role=hub admin -> hub getdata get userId != userid -> 자신의 허브만 업체 생성 가능


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

    @Transactional(readOnly = true)
    public Page<VendorResponse> getVendors(Pageable pageable, String keyword) {
        Page<Vendor> vendors = vendorRepository.findAll(pageable);
        List<VendorResponse> contents = vendors.getContent().stream().map(VendorResponse::of).toList();
        return new PageImpl<>(contents, pageable, vendors.getSize());
    }

    public VendorResponse getVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
        return VendorResponse.of(vendor);
    }

    @Transactional
    public VendorResponse updateVendor(UUID vendorId,UpdateVendorDto request) {

        //role=hub admin -> hub getdata get userId != userid -> 자신의 허브만 업체 수정 가능
        //vendor.getuserId != userid -> 자신의 업체만 수정 가능

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

//        if(!hubClient.getHub(request.getHubId()).getCode().equals(20000)) {
//            throw new CustomApiException(ResErrorCode.NOT_FOUND);
//        }
//
//        if(!userClient.getUser(request.getUserId()).getCode().equals(20000)) {
//            throw new CustomApiException(ResErrorCode.NOT_FOUND);
//        }


        vendor.update(
                request.getHubId(),
                request.getUserId(),
                request.getVendorName(),
                request.getVendorType(),
                request.getVendorAddress()
        );

        return VendorResponse.of(vendor);

    }

    public VendorResponse deleteVendor(UUID vendorId) {

        //role=hub admin -> hub getdata get userId != userid -> 자신의 허브만 업체 삭제 가능

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        vendorRepository.delete(vendor);

        return VendorResponse.of(vendor);

    }
}
