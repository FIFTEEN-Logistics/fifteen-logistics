package com.sparta.fifteen.vpo.domain.repository;

import com.sparta.fifteen.vpo.domain.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
}
