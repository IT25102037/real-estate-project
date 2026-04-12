package com.realestate.project.controller; // Make sure this matches your actual package name!

import com.realestate.project.model.Customer;
import com.realestate.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // This prevents the CORS errors we fixed earlier!
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    // 1. YOUR EXISTING SAVE METHOD (Create)
    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    // 2. THE NEW GET ALL METHOD (Read)
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 3. THE NEW DELETE METHOD
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        // This tells MySQL to delete the row with this specific ID
        customerRepository.deleteById(id);
    }
}