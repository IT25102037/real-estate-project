package com.realestate.project.service;

import com.realestate.project.model.Property;
import com.realestate.project.model.House;
import com.realestate.project.model.Apartment;
import com.realestate.project.model.RentalProperty;
import com.realestate.project.repository.PropertyImageRepository;
import com.realestate.project.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final ActivityService activityService;

    public PropertyService(PropertyRepository propertyRepository, PropertyImageRepository propertyImageRepository, ActivityService activityService){
        this.propertyRepository = propertyRepository;
        this.propertyImageRepository = propertyImageRepository;
        this.activityService = activityService;
    }

    /// save a property
    public Property saveProperty(Property property){
        Property saved = propertyRepository.save(property);
        activityService.logActivity(
                "PROPERTY_CREATED",
                "<strong>New listing</strong> — " + saved.getTitle() + " in " + saved.getLocation(),
                "check-circle",
                "green"
        );
        return saved;
    }
    /// return a list of many properties
    public List <Property> getAllProperties(){
        return propertyRepository.findAll();
    }
    /// return a property if exits , if not no property
    public Optional <Property> getPropertyById(long id){
        return propertyRepository.findById(id);
    }
    /// deleting a property object
    @Transactional
    public void deleteProperty(long id){
        propertyRepository.findById(id).ifPresent(property -> {
            propertyImageRepository.deleteByPropertyId(id);
            propertyRepository.delete(property);
            activityService.logActivity(
                    "PROPERTY_DELETED",
                    "<strong>Listing removed</strong> — " + property.getTitle(),
                    "trash-2",
                    "red"
            );
        });
    }

    /// updating a property
    public Property updateProperty(long id, Property updatedProperty){
        return propertyRepository.findById(id).map( existingProperty-> {

            if (!existingProperty.getClass().equals(updatedProperty.getClass())){
                throw new RuntimeException("Property type mis-match");
            }

            /// update parent fields
            existingProperty.setTitle(updatedProperty.getTitle());
            existingProperty.setLocation(updatedProperty.getLocation());
            existingProperty.setPrice(updatedProperty.getPrice());
            existingProperty.setDescription(updatedProperty.getDescription());

            /// update child fields
            if (existingProperty instanceof House && updatedProperty instanceof House){
                ((House)existingProperty).setNumOfFloors(((House) updatedProperty).getNumOfFloors());
            } else if (existingProperty instanceof Apartment && updatedProperty instanceof Apartment ) {
                ((Apartment)existingProperty).setFloorNumber(((Apartment) updatedProperty).getFloorNumber());
            } else if (existingProperty instanceof  RentalProperty && updatedProperty instanceof RentalProperty) {
                ((RentalProperty)existingProperty).setMonthlyRent(((RentalProperty)updatedProperty).getMonthlyRent());
                ((RentalProperty)existingProperty).setAdvanceAmount(((RentalProperty)updatedProperty).getAdvanceAmount());
                ((RentalProperty)existingProperty).setDuration(((RentalProperty)updatedProperty).getDuration());
            }

            Property saved = propertyRepository.save(existingProperty);
            activityService.logActivity(
                    "PROPERTY_UPDATED",
                    "<strong>Listing updated</strong> — " + saved.getTitle() + " details updated",
                    "pencil",
                    "blue"
            );
            return saved;
        }).orElseThrow(()-> new RuntimeException("Property not Found"));
    }
}
