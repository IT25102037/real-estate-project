package com.realestate.project.controller;

import com.realestate.project.model.Customer;
import com.realestate.project.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*") // ADD THIS LINE TO ALLOW THE FRONTEND TO TALK TO THE BACKEND
public class CustomerController {

    private final CustomerService customerService;

    // By defining this constructor, Spring automatically injects CustomerService.

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        // Now 'this.customerService' will be properly initialized
        return customerService.saveCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "Customer deleted successfully";
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer details) {
        return customerService.updateCustomer(id, details);
    }
}