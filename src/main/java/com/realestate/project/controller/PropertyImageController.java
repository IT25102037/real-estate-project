package com.realestate.project.controller;

import com.realestate.project.model.PropertyImage;
import com.realestate.project.service.PropertyImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    public PropertyImageController(PropertyImageService propertyImageService) {
        this.propertyImageService = propertyImageService;
    }

    @GetMapping("/property/{propertyId}")
    public List<Map<String, Object>> getImagesByProperty(@PathVariable Long propertyId) {
        return propertyImageService.getImagesByPropertyId(propertyId)
                .stream()
                .map(image -> Map.<String, Object>of(
                        "id", image.getId(),
                        "fileName", image.getFileName() == null ? "property-image" : image.getFileName(),
                        "fileType", image.getFileType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : image.getFileType(),
                        "url", "/images/" + image.getId() + "/data"
                ))
                .toList();
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<byte[]> getImageData(@PathVariable Long id) {
        PropertyImage image = propertyImageService.getImageById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(image.getFileType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : image.getFileType()))
                .body(image.getData());
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
