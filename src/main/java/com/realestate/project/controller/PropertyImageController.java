package com.realestate.project.controller;

import com.realestate.project.service.PropertyImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    public PropertyImageController(PropertyImageService propertyImageService) {
        this.propertyImageService = propertyImageService;
    }


    @PostMapping("/{id}/update")
    public String updateImage(@PathVariable long id,
                              @RequestParam("newImage")MultipartFile newImage,
                              @RequestParam("propertyId")long propertyId) throws IOException{
        propertyImageService.updateImage(id,newImage);
        return "redirect:/properties/" + propertyId;
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteImage(@PathVariable Long id) {
        propertyImageService.deleteImageById(id);
        return "Image deleted successfully";
    }

    @DeleteMapping("/property/{propertyId}")
    @ResponseBody
    public String deleteImagesByProperty(@PathVariable Long propertyId) {
        propertyImageService.deleteImagesByPropertyId(propertyId);
        return "All images deleted for this property";
    }
}
