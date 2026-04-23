package com.realestate.project.service;

import com.realestate.project.model.Customer;
import com.realestate.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // 1. Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 2. Save a customer
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // 3. Delete a customercustomerservice.java file
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // 4. Update a customer
    public Customer updateCustomer(Long id, Customer details) {
        return customerRepository.findById(id).map(customer -> {
            // Only update if the new value is not null or empty
            if (details.getName() != null) customer.setName(details.getName());
            if (details.getEmail() != null) customer.setEmail(details.getEmail());
            if (details.getPhone() != null) customer.setPhone(details.getPhone());
            if (details.getPropertyType() != null) customer.setPropertyType(details.getPropertyType());
            if (details.getAddress() != null) customer.setAddress(details.getAddress());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}