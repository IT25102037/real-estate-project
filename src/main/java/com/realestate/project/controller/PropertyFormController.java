package com.realestate.project.controller;

import com.realestate.project.model.Apartment;
import com.realestate.project.model.House;
import com.realestate.project.model.Property;
import com.realestate.project.model.PropertyImage;
import com.realestate.project.model.RentalProperty;
import com.realestate.project.repository.PropertyImageRepository;
import com.realestate.project.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PropertyFormController {

    private final PropertyService propertyService;
    private final PropertyImageRepository propertyImageRepository;

    public PropertyFormController(PropertyService propertyService, PropertyImageRepository propertyImageRepository) {
        this.propertyService = propertyService;
        this.propertyImageRepository = propertyImageRepository;
    }

    @GetMapping("/properties/new/house")
    public String showHouseForm() {
        return "house-form";
    }

    @GetMapping("/properties/new/apartment")
    public String showApartmentForm() {
        return "apartment-form";
    }

    @GetMapping("/properties/new/rental")
    public String showRentalForm() {
        return "rental-form";
    }

    @PostMapping("/properties/save/house")
    public String saveHouse(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam double price,
            @RequestParam String description,
            @RequestParam int numOfFloors,
            @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        House house = new House(title, location, price, description, numOfFloors);
        Property savedProperty = propertyService.saveProperty(house);
        saveImages(savedProperty, images);
        return "redirect:/sell";
    }

    @PostMapping("/properties/save/apartment")
    public String saveApartment(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam double price,
            @RequestParam String description,
            @RequestParam int floorNumber,
            @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        Apartment apartment = new Apartment(title, location, price, description, floorNumber);
        Property savedProperty = propertyService.saveProperty(apartment);
        saveImages(savedProperty, images);
        return "redirect:/sell";
    }

    @PostMapping("/properties/save/rental")
    public String saveRental(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam double price,
            @RequestParam String description,
            @RequestParam double monthlyRent,
            @RequestParam(defaultValue = "0") double advanceAmount,
            @RequestParam(required = false) String duration,
            @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        RentalProperty rental = new RentalProperty(title, location, price, description, monthlyRent, advanceAmount, duration);
        Property savedProperty = propertyService.saveProperty(rental);
        saveImages(savedProperty, images);
        return "redirect:/sell";
    }

    @PostMapping("/api/properties/{id}/images")
    @ResponseBody
    public String addImagesToProperty(
            @PathVariable Long id,
            @RequestParam("images") MultipartFile[] images
    ) throws IOException {
        Property savedProperty = propertyService.getPropertyById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        saveImages(savedProperty, images);
        return "Images added successfully";
    }

    private void saveImages(Property savedProperty, MultipartFile[] images) throws IOException {
        if (images == null) {
            return;
        }

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                PropertyImage propertyImage = new PropertyImage(
                        image.getOriginalFilename(),
                        image.getContentType(),
                        image.getBytes(),
                        savedProperty
                );
                propertyImageRepository.save(propertyImage);
            }
        }
    }
}
