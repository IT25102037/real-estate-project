package com.realestate.project.controller;

import com.realestate.project.model.ContactMessage;
import com.realestate.project.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*") // Allows your HTML to talk to this Java file
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping
    public ContactMessage saveMessage(@RequestBody ContactMessage message) {
        return contactRepository.save(message);
    }

    @GetMapping
    public List<ContactMessage> getAllMessages() {
        return contactRepository.findAll();
    }
}
