package com.realestate.project.service;

import com.realestate.project.model.ContactMessage;
import com.realestate.project.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public ContactMessage saveMessage(ContactMessage message) {
        return contactRepository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return contactRepository.findAll();
    }

    public boolean deleteMessage(Long id) {
        return contactRepository.findById(id).map(message -> {
            contactRepository.delete(message);
            return true;
        }).orElse(false);
    }

    public Optional<ContactMessage> updateStatus(Long id, String newStatus) {
        return contactRepository.findById(id).map(message -> {
            // Business logic: cleaning the string
            String cleanStatus = newStatus.replace("\"", "");
            message.setStatus(cleanStatus);
            return contactRepository.save(message);
        });
    }
}