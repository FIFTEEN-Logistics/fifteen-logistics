package com.fifteen.eureka.vpo.domain.repository;

import com.fifteen.eureka.vpo.domain.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository{
    Optional<Vendor> findById(UUID vendorId);

    void delete(Vendor vendor);

    Vendor save(Vendor vendor);

    Page<Vendor> findAll(Pageable pageable);

    Optional<Vendor> findByVendorName(String vendorName);
}
