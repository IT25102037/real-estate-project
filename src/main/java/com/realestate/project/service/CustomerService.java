package com.realestate.project.service;

import com.realestate.project.dto.CustomerDto;
import com.realestate.project.model.CustomerEntity;
import com.realestate.project.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ActivityService activityService;

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ── NEW: get a single customer by ID (used by profile.html) ──
    public Optional<CustomerEntity> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // Adding @Transactional ensures the data is "Committed" to MySQL
    @Transactional
    public CustomerEntity saveCustomer(CustomerDto customerDto) {
        // ── NEW: duplicate email validation ──
        if (customerRepository.findByEmail(customerDto.getEmail()).isPresent()) {
            throw new RuntimeException("EMAIL_ALREADY_EXISTS");
        }
        CustomerEntity customerEntity = modelMapper.map(customerDto, CustomerEntity.class);
        CustomerEntity saved = customerRepository.save(customerEntity);
        activityService.logActivity(
                "CUSTOMER_REGISTERED",
                "<strong>New customer</strong> — " + saved.getName() + " registered",
                "user-plus",
                "blue"
        );
        return saved;
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
        customerRepository.findById(id).ifPresent(customer -> {
            customerRepository.delete(customer);
            activityService.logActivity(
                    "CUSTOMER_DELETED",
                    "<strong>Customer profile deleted</strong> — " + customer.getName(),
                    "trash-2",
                    "red"
            );
        });
    }

    @Transactional
    public CustomerEntity updateCustomer(Long id, CustomerEntity details) {
        return customerRepository.findById(id).map(customer -> {
            if (details.getName() != null) customer.setName(details.getName());
            if (details.getEmail() != null) customer.setEmail(details.getEmail());
            if (details.getPhone() != null) customer.setPhone(details.getPhone());
            if (details.getPropertyType() != null) customer.setPropertyType(details.getPropertyType());
            if (details.getAddress() != null) customer.setAddress(details.getAddress());
            if (details.getPassword() != null) customer.setPassword(details.getPassword());

            CustomerEntity saved = customerRepository.save(customer);
            activityService.logActivity(
                    "CUSTOMER_UPDATED",
                    "<strong>Customer profile updated</strong> — " + saved.getName(),
                    "pencil",
                    "blue"
            );
            return saved;
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}