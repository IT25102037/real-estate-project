package com.realestate.project.controller;

import com.realestate.project.dto.CustomerDto;
import com.realestate.project.dto.SignInRequest;
import com.realestate.project.model.CustomerEntity;
import com.realestate.project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> register(@RequestBody CustomerDto customerDto) {
        System.out.println("Received : " + customerDto);
        try {
            CustomerEntity savedCustomer = customerService.saveCustomer(customerDto);
            return ResponseEntity.ok(savedCustomer);
        } catch (RuntimeException e) {
            if ("EMAIL_ALREADY_EXISTS".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("EMAIL_ALREADY_EXISTS");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        try {
            // Authenticate the user
            CustomerEntity customer = customerService.login(
                    signInRequest.getEmail(),
                    signInRequest.getPassword()
            );

            // Send back the customer data (Frontend needs data.name)
            return ResponseEntity.ok(customer);

        } catch (RuntimeException e) {
            // If login fails, return 401 (Unauthorized)
            // This triggers the .catch() in your JavaScript
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // 2. GET ALL FROM DATABASE
    // This matches: fetch('http://localhost:8080/api/customers/')
    @GetMapping
    public List<CustomerEntity> getAll() {
        return customerService.getAllCustomers();
    }

    // ── NEW: GET SINGLE CUSTOMER BY ID ──
    // Used by profile.html to fetch the logged-in user's latest data from the database.
    // This matches: fetch(`http://localhost:8080/api/customers/${id}`)
    @GetMapping("/{id}")
    public ResponseEntity<CustomerEntity> getById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

