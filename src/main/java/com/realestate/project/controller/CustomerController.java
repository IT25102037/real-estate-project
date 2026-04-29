package com.realestate.project.controller;

import com.realestate.project.dto.CustomerDto;
import com.realestate.project.model.CustomerEntity;
import com.realestate.project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // 1. SAVE TO DATABASE
    // This matches: fetch("http://localhost:8080/api/customers/register", ...)
    @PostMapping("/register")
    public ResponseEntity<CustomerDto> register(@RequestBody CustomerDto customerDto) {
        System.out.println("Received : " +customerDto);
        // This calls the service which saves to MySQL
        customerService.saveCustomer(customerDto);
        return ResponseEntity.ok(customerDto);
    }

    // 2. GET FROM DATABASE
    // This matches: fetch('http://localhost:8080/api/customers/')
    @GetMapping
    public List<CustomerEntity> getAll() {
        return customerService.getAllCustomers();
    }

    // 3. DELETE FROM DATABASE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    // 4. UPDATE IN DATABASE
    @PutMapping("/{id}")
    public ResponseEntity<CustomerEntity> update(@PathVariable Long id, @RequestBody CustomerEntity details) {
        CustomerEntity updated = customerService.updateCustomer(id, details);
        return ResponseEntity.ok(updated);
    }
}