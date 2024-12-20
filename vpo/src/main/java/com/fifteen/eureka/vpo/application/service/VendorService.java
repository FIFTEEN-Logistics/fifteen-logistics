package com.fifteen.eureka.vpo.application.service;

import com.fifteen.eureka.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.common.response.ResErrorCode;
import com.fifteen.eureka.common.role.Role;
import com.fifteen.eureka.vpo.application.dto.vendor.CreateVendorDto;
import com.fifteen.eureka.vpo.application.dto.vendor.UpdateVendorDto;
import com.fifteen.eureka.vpo.application.dto.vendor.VendorResponse;
import com.fifteen.eureka.vpo.domain.model.Order;
import com.fifteen.eureka.vpo.domain.model.Vendor;
import com.fifteen.eureka.vpo.domain.model.VendorType;
import com.fifteen.eureka.vpo.domain.repository.VendorRepository;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryClient;
import com.fifteen.eureka.vpo.infrastructure.client.delivery.DeliveryDetailsResponse;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubClient;
import com.fifteen.eureka.vpo.infrastructure.client.hub.HubDetailsResponse;
import com.fifteen.eureka.vpo.infrastructure.client.user.UserClient;
import com.fifteen.eureka.vpo.infrastructure.client.user.UserGetResponseDto;
import com.fifteen.eureka.vpo.infrastructure.repository.VendorQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorQueryRepository vendorQueryRepository;
    private final HubClient hubClient;
    private final UserClient userClient;
    private final DeliveryClient deliveryClient;

    @Transactional
    public VendorResponse createVendor(CreateVendorDto request, Long currentUserId, String currentRole) {

        vendorRepository.findByVendorName(request.getVendorName())
                .ifPresent(existingVendor -> {
                    if (Objects.equals(existingVendor.getHubId(), request.getHubId())) {
                        throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 허브에 중복된 업체 이름이 존재합니다.");
                    }
                });

        HubDetailsResponse hubDetailsResponse = Optional.ofNullable(hubClient.getHub(request.getHubId()).getData())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST));

        if(currentRole.equals("ROLE_ADMIN_HUB")) {
            if (!Objects.equals(hubDetailsResponse.getHubManagerId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "해당 허브 담당자가 아닌 경우 업체 생성이 불가합니다.");
            }
        }

        UserGetResponseDto userGetResponseDto = Optional.ofNullable(userClient.findUserByIdForService(request.getUserId()).getBody().getData())
                .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));

        if (!userGetResponseDto.getRole().equals(Role.ROLE_ADMIN_VENDOR)) {
            throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "업체 담당자 권한을 가진 사용자가 아닙니다.");
        }

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
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 업체를 찾을 수 없습니다."));
        return VendorResponse.of(vendor);
    }

    @Transactional
    public VendorResponse updateVendor(UUID vendorId, UpdateVendorDto request, Long currentUserId, String currentRole) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 업체를 찾을 수 없습니다."));

        // 허브담당자
        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            if (!Objects.equals(vendor.getHubManagerId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "해당 허브 담당자가 아닌 경우 업체 수정이 불가합니다.");
            }
        }

        // 업체담당자
        if (currentRole.equals("ROLE_ADMIN_VENDOR")) {
            if (!Objects.equals(vendor.getUserId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "해당 업체 담당자가 아닌 경우 업체 수정이 불가합니다.");
            }
        }

        // hubId 변경 되었을 경우
        if (!Objects.equals(vendor.getHubId(), request.getHubId())) {

            HubDetailsResponse hubDetailsResponse = Optional.ofNullable(hubClient.getHub(request.getHubId()).getData())
                    .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST));

            vendor.hunInfoUpdate(hubDetailsResponse.getId(), hubDetailsResponse.getHubManagerId());
        }

        // vendor manager 변경되었을 경우
        if (!Objects.equals(vendor.getUserId(), request.getUserId())) {
            UserGetResponseDto userGetResponseDto = Optional.ofNullable(userClient.findUserByIdForService(currentUserId).getBody().getData())
                .orElseThrow(()-> new CustomApiException(ResErrorCode.BAD_REQUEST));

            if (!userGetResponseDto.getRole().equals(Role.ROLE_ADMIN_VENDOR)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "업체 담당자 권한을 가진 사용자가 아닙니다.");
            }
        }

        vendorRepository.findByVendorName(request.getVendorName())
                .ifPresent(existingVendor -> {
                    if (Objects.equals(existingVendor.getHubId(), request.getHubId())) {
                        throw new CustomApiException(ResErrorCode.BAD_REQUEST, "해당 허브에 중복된 업체 이름이 존재합니다.");
                    }
                });

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
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 업체를 찾을 수 없습니다."));

        if (currentRole.equals("ROLE_ADMIN_HUB")) {
            if (!Objects.equals(vendor.getHubManagerId(), currentUserId)) {
                throw new CustomApiException(ResErrorCode.UNAUTHORIZED);
            }
        }

        if (vendor.getVendorType().equals(VendorType.SUPPLIER)) {
            for (Order suppliedOrder : vendor.getSuppliedOrders()) {
                if (!suppliedOrder.isCanceled()) {
                    checkDeliveryStatus(suppliedOrder.getDeliveryId());
                }
            }
        } else {
            for (Order receivedOrder : vendor.getReceivedOrders()) {
                if (!receivedOrder.isCanceled()) {
                    checkDeliveryStatus(receivedOrder.getDeliveryId());
                }
            }
        }

        vendor.markAsDeleted();

        return VendorResponse.of(vendor);

    }

    public void checkDeliveryStatus(UUID deliveryId) {
        DeliveryDetailsResponse deliveryDetailsResponse = (DeliveryDetailsResponse) Optional.ofNullable(deliveryClient.getDelivery(deliveryId).getData())
                .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 배송을 찾을 수 없습니다."));

        if (!DeliveryDetailsResponse.DeliveryStatus.DST_ARRIVED.equals(deliveryDetailsResponse.getDeliveryStatus())) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "배달 진행중인 상품은 삭제할 수 없습니다.");
        }
    }
}
