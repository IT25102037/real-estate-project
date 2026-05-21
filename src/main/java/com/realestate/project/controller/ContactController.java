package com.realestate.project.controller;

import com.realestate.project.model.ContactMessage;
import com.realestate.project.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ContactMessage saveMessage(@RequestBody ContactMessage message) {
        return contactService.saveMessage(message);
    }

    @GetMapping
    public List<ContactMessage> getAllMessages() {
        return contactService.getAllMessages();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        boolean deleted = contactService.deleteMessage(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ContactMessage> updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        return contactService.updateStatus(id, newStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}