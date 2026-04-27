package com.realestate.project.service;

import com.realestate.project.model.PropertyImage;
import com.realestate.project.repository.PropertyImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PropertyImageService {

    private final PropertyImageRepository propertyImageRepository;

    public PropertyImageService(PropertyImageRepository propertyImageRepository) {
        this.propertyImageRepository = propertyImageRepository;
    }

    @Transactional
    public void deleteImageById(Long id) {
        if (!propertyImageRepository.existsById(id)) {
            throw new RuntimeException("Image not found");
        }

        propertyImageRepository.deleteByPropertyId(id);
    }

    @Transactional
    public void deleteImagesByPropertyId(Long propertyId) {
        propertyImageRepository.deleteByPropertyId(propertyId);
    }

    @Transactional
    public PropertyImage updateImage (long imageId, MultipartFile newImage) throws IOException {
        PropertyImage existingImage = propertyImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image is not found"));

        existingImage.setFileName(newImage.getOriginalFilename());
        existingImage.setFileType(newImage.getContentType());
        existingImage.setData(newImage.getBytes());

        return propertyImageRepository.save(existingImage);

    }


}
