package com.realestate.project.controller;

import com.realestate.project.model.ContactMessage;
import com.realestate.project.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*") // Allows your HTML to talk to this Java file
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    /**
     * Save a new message from the contact form
     */
    @PostMapping
    public ContactMessage saveMessage(@RequestBody ContactMessage message) {
        return contactRepository.save(message);   //Abstraction
    }

    /**
     * Get all messages for the Admin View-Messages table
     */
    @GetMapping
    public List<ContactMessage> getAllMessages() {
        return contactRepository.findAll();  //Abstraction
    }

    /**
     * Delete a message from the database by its ID
     * This is triggered by the 'Remove' button in your HTML
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        return contactRepository.findById(id)
                .map(message -> {
                    contactRepository.delete(message);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ContactMessage> updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        return contactRepository.findById(id)
                .map(message -> {
                    // Remove extra quotes if the string comes in with them
                    String cleanStatus = newStatus.replace("\"", "");
                    message.setStatus(cleanStatus);
                    return ResponseEntity.ok(contactRepository.save(message));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}