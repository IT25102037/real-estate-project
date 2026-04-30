package com.realestate.project.service;

import com.realestate.project.dto.CustomerDto;
import com.realestate.project.model.CustomerEntity;
import com.realestate.project.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import this

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Adding @Transactional ensures the data is "Committed" to MySQL
    @Transactional
    public void saveCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = modelMapper.map(customerDto, CustomerEntity.class);
        customerRepository.save(customerEntity);
    }

    @Transactional(readOnly = true)
    public CustomerEntity login(String email, String password) {
        // 1. Look for the user by email
        CustomerEntity customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Check if the password matches (Plain text for now)
        if (!customer.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Return the customer object if successful
        return customer;
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public CustomerEntity updateCustomer(Long id, CustomerEntity details) {
        return customerRepository.findById(id).map(customer -> {
            if (details.getName() != null) customer.setName(details.getName());
            if (details.getEmail() != null) customer.setEmail(details.getEmail());
            if (details.getPhone() != null) customer.setPhone(details.getPhone());
            if (details.getPropertyType() != null) customer.setPropertyType(details.getPropertyType());
            if (details.getAddress() != null) customer.setAddress(details.getAddress());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}