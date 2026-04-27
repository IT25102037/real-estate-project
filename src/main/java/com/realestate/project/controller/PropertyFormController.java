package com.realestate.project.controller;

import com.realestate.project.model.House;
import com.realestate.project.model.Apartment;
import com.realestate.project.model.RentalProperty;
import com.realestate.project.model.Property;
import com.realestate.project.model.PropertyImage;
import com.realestate.project.repository.PropertyImageRepository;
import com.realestate.project.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PropertyFormController {

    private final PropertyService propertyService;
    private final PropertyImageRepository propertyImageRepository;

    public PropertyFormController(PropertyService propertyService, PropertyImageRepository propertyImageRepository){
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


    /// for house(house details with images )
    @PostMapping("/properties/save/house")
    public String saveHouse(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam double price,
            @RequestParam String description,
            @RequestParam int numOfFloors,
            @RequestParam("images")MultipartFile[]images
    )throws  IOException{

        /// saving the house details
        House house = new House(title,location,price,description,numOfFloors);
        Property savedProperty = propertyService.saveProperty(house);

        /// saving the images of the house
        for (MultipartFile image:images){
            if (!image.isEmpty()){
                PropertyImage propertyImage = new PropertyImage(
                        image.getOriginalFilename(),
                        image.getContentType(),
                        image.getBytes(),
                        savedProperty
                );
                propertyImageRepository.save(propertyImage);


            }
        }
        return "redirect:/properties";
    }

    /// for apartment (apartment details with images )
    @PostMapping("/properties/save/apartment")
    public String saveApartment(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam double price,
            @RequestParam String description,
            @RequestParam int floorNumber,
            @RequestParam("images") MultipartFile[] images
    ) throws IOException {

        Apartment apartment = new Apartment(title, location, price, description, floorNumber);
        Property savedProperty = propertyService.saveProperty(apartment);

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

        return "redirect:/properties";
    }

    /// for rental property (rental details with images)
    @PostMapping("/properties/save/rental")
    public String saveRental(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam double price,
            @RequestParam String description,
            @RequestParam double monthlyRent,
            @RequestParam("images") MultipartFile[] images
    ) throws IOException {

        RentalProperty rental = new RentalProperty(title, location, price, description, monthlyRent);
        Property savedProperty = propertyService.saveProperty(rental);

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

        return "redirect:/properties";
    }









}
