package com.realestate.project.repository;

import com.realestate.project.model.PropertyImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {

    List<PropertyImage> findByPropertyId(Long propertyId);

    @Transactional
    void deleteByPropertyId(Long propertyId);
}
