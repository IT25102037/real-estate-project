package com.realestate.project.repository;

import com.realestate.project.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    // Leave this empty! JpaRepository provides .save() for you.
}