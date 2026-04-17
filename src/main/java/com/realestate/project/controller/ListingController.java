package com.realestate.project.controller;

import com.realestate.project.model.Listing;
import com.realestate.project.repository.ListingRepository; // Ensure this is imported
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "*")
public class ListingController {

    @Autowired
    private ListingRepository listingRepository; // This must match the variable name used below

    @PostMapping("/add")
    public Listing addListing(@RequestBody Listing listing) {
        return listingRepository.save(listing); // This will now work
    }

    @GetMapping("/all")
    public List<Listing> getAllListings() {
        return listingRepository.findAll(); // This will now work
    }
}
