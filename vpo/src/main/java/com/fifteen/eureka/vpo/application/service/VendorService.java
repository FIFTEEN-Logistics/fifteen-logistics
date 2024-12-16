package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.vpo.application.dto.vendor.CreateVendorDto;
import com.fifteen.eureka.vpo.application.dto.vendor.UpdateVendorDto;
import com.fifteen.eureka.vpo.application.dto.vendor.VendorResponse;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubClient;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubDetailsResponse;
import com.fifteen.eureka.vpo.infrastructure.client.user.UserClient;
import com.fifteen.eureka.vpo.infrastructure.repository.VendorQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorQueryRepository vendorQueryRepository;
    private final HubClient hubClient;
    private final UserClient userClient;

    @Transactional
    public VendorResponse createVendor(CreateVendorDto request, Long currentUserId, String currentRole) {

        HubDetailsResponse hubDetailsResponse = Optional.ofNullable(
                hubClient.getHub(request.getHubId()).getData())
                    .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));

        if(currentRole.equals("ROLE_ADMIN_HUB")) {
            if(!hubDetailsResponse.getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }


//        UserGetResponseDto userGetResponseDto = Optional.ofNullable(
//                (UserGetResponseDto) userClient.findUserById(currentUserId,"username","ROLE_ADMIN_MASTER").getBody().getData()
//                .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));
//
//        if (!userGetResponseDto.getRole().equals(UserGetResponseDto.Role.ROLE_ADMIN_VENDOR)) {
//            throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
//        }

        Vendor vendor = Vendor.create(
                request.getHubId(),
                request.getUserId(), //업체 담당자 ID
                hubDetailsResponse.getHubManagerId(),
                request.getVendorName(),
                request.getVendorType(),
                request.getVendorAddress()
        );

        vendorRepository.save(vendor);

        return VendorResponse.of(vendor);
    }

    @Transactional(readOnly = true)
    public PagedModel<VendorResponse> getVendors(Pageable pageable, String keyword) {

        Page<Vendor> vendors = vendorQueryRepository.findByKeyword(keyword,pageable);

        List<VendorResponse> contents = vendors.getContent().stream().map(VendorResponse::of).toList();

        return new PagedModel<>(new PageImpl<>(contents, pageable, vendors.getTotalElements()));
    }

    public VendorResponse getVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));
        return VendorResponse.of(vendor);
    }

    @Transactional
    public VendorResponse updateVendor(UUID vendorId, UpdateVendorDto request, Long currentUserId, String currentRole) {

        //role=hub admin -> hub getdata get userId != userid -> 자신의 허브만 업체 수정 가능
        //vendor.getuserId != userid -> 자신의 업체만 수정 가능
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        // 허브담당자
        if (currentRole.equals("ROLE_ADMIN_HUB")) {

            HubDetailsResponse hubDetailsResponse = Optional.ofNullable(
                            hubClient.getHub(vendor.getHubId()).getData())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST));

            if (!hubDetailsResponse.getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        // 업체담당자
        if (currentRole.equals("ROLE_ADMIN_VENDOR")) {
            if (!vendor.getUserId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        // 수정하려는 hubId 존재하는지 검사
        if (!vendor.getHubId().equals(request.getHubId())) {
            HubDetailsResponse hubDetailsResponse = Optional.ofNullable(
                            hubClient.getHub(request.getHubId()).getData())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST));

            vendor.hunInfoUpdate(hubDetailsResponse.getId(), hubDetailsResponse.getHubManagerId());
        }


        if (!vendor.getUserId().equals(request.getUserId())) {
            if (!userClient.findUserById(
                    request.getUserId(),
                    "test",
                    "ROLE_ADMIN_MASTER").getStatusCode().equals(HttpStatusCode.valueOf(200))) {
                throw new CustomApiException(ResErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
            }
        }

        vendor.update(
                request.getUserId(),
                request.getVendorName(),
                request.getVendorType(),
                request.getVendorAddress()
        );

        return VendorResponse.of(vendor);
    }

    public VendorResponse deleteVendor(UUID vendorId, Long currentUserId, String currentRole) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND));

        // 허브담당자
        if(currentRole.equals("ROLE_ADMIN_HUB")) {

            HubDetailsResponse hubDetailsResponse = Optional.ofNullable(
                            hubClient.getHub(vendor.getHubId()).getData())
                    .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));

            if(!hubDetailsResponse.getHubManagerId().equals(currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }


        vendorRepository.delete(vendor);

        return VendorResponse.of(vendor);

    }
}
