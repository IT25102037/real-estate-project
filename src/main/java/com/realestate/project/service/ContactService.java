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

    @Autowired
    private ActivityService activityService;

    public ContactMessage saveMessage(ContactMessage message) {
        ContactMessage saved = contactRepository.save(message);
        activityService.logActivity(
                "MESSAGE_RECEIVED",
                "<strong>New inquiry</strong> — " + saved.getUserName() + " submitted a message",
                "mail",
                "gold"
        );
        return saved;
    }

    public List<ContactMessage> getAllMessages() {
        return contactRepository.findAll();
    }

    public boolean deleteMessage(Long id) {
        return contactRepository.findById(id).map(message -> {
            contactRepository.delete(message);
            activityService.logActivity(
                    "MESSAGE_DELETED",
                    "<strong>Message deleted</strong> — from " + message.getUserName(),
                    "trash-2",
                    "red"
            );
            return true;
        }).orElse(false);
    }

    public Optional<ContactMessage> updateStatus(Long id, String newStatus) {
        return contactRepository.findById(id).map(message -> {
            // Business logic: cleaning the string
            String cleanStatus = newStatus.replace("\"", "");
            message.setStatus(cleanStatus);
            ContactMessage saved = contactRepository.save(message);
            activityService.logActivity(
                    "MESSAGE_STATUS_UPDATED",
                    "<strong>Inquiry status updated</strong> — " + saved.getUserName() + "'s message set to " + saved.getStatus(),
                    "check-circle",
                    "green"
            );
            return saved;
        });
    }
}