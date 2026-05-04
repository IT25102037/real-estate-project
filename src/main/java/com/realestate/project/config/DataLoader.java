package com.realestate.project.config;

import com.realestate.project.entity.Property;
import com.realestate.project.entity.User;
import com.realestate.project.repo.PropertyRepository;
import com.realestate.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user1 = new User(null, "John Doe", "john@example.com", null);
            User user2 = new User(null, "Jane Smith", "jane@example.com", null);
            userRepository.save(user1);
            userRepository.save(user2);
        }

        if (propertyRepository.count() == 0) {
            Property prop1 = new Property(null, "Luxury Villa", "123 Ocean Dr", 500000.0, null);
            Property prop2 = new Property(null, "City Apartment", "456 Downtown Ave", 150000.0, null);
            propertyRepository.save(prop1);
            propertyRepository.save(prop2);
        }
    }
}
