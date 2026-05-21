package com.realestate.project.controller;

import com.realestate.project.model.Property;
import com.realestate.project.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    /// creating a Property
    @PostMapping
    public Property createProperty(@Valid @RequestBody Property property){
        return propertyService.saveProperty(property);
    }

    /// reading all properties
    @GetMapping
    public List<Property> getAllProperties(){
        return propertyService.getAllProperties();
    }

    /// read one property by ID
    @GetMapping("/{id}")
    public Property getPropertyById(@PathVariable Long id){
        return propertyService.getPropertyById(id)
                .orElseThrow(()->new  RuntimeException("Property Not Found"));
    }

    /// update a property
    @PutMapping("/{id}")
    public Property updateProperty(@PathVariable Long id,
                                   @Valid @RequestBody Property updatedProperty){
        return propertyService.updateProperty(id, updatedProperty);
    }

    /// Delete a property by ID
    @DeleteMapping("/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return "Property deleted successfully";
    }

}
