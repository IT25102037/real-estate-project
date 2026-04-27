package com.realestate.project.service;

import com.realestate.project.model.Property;
import com.realestate.project.model.House;
import com.realestate.project.model.Apartment;
import com.realestate.project.model.RentalProperty;
import com.realestate.project.repository.PropertyRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;




@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository){
        this.propertyRepository = propertyRepository;
    }

    /// save a property
    public Property saveProperty(Property property){
        return propertyRepository.save(property);
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
    public void deleteProperty(long id){
        propertyRepository.deleteById(id);


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
            }

            return propertyRepository.save(existingProperty);


        }).orElseThrow(()-> new RuntimeException("Property not Found"));



    }

}
