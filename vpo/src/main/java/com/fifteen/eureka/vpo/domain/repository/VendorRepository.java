package com.fifteen.eureka.vpo.domain.repository;

import com.fifteen.eureka.vpo.domain.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface VendorRepository{
    Optional<Vendor> findById(UUID vendorId);

    void delete(Vendor vendor);

    void save(Vendor vendor);

    Page<Vendor> findAll(Pageable pageable);
}
