package com.realestate.project.controller;

import jakarta.validation.Valid;
import com.realestate.project.dto.PaymentDTO;
import com.realestate.project.entity.PaymentStatus;
import com.realestate.project.entity.PaymentType;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.realestate.project.repo.PropertyRepository;
import com.realestate.project.repo.UserRepository;
import com.realestate.project.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payments";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("payment", new PaymentDTO());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("properties", propertyRepository.findAll());
        model.addAttribute("paymentTypes", PaymentType.values());
        return "payment-form";
    }

    @PostMapping("/save")
    public String savePayment(@Valid @ModelAttribute("payment") PaymentDTO paymentDTO,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("properties", propertyRepository.findAll());
            model.addAttribute("paymentTypes", PaymentType.values());
            return "payment-form";
        }
        
        try {
            paymentService.createPayment(paymentDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("properties", propertyRepository.findAll());
            model.addAttribute("paymentTypes", PaymentType.values());
            return "payment-form";
        }
        
        return "redirect:/payments";
    }
    
    @GetMapping("/{id}/invoice")
    public String viewInvoice(@PathVariable Long id, Model model) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        model.addAttribute("payment", payment);
        return "invoice";
    }

    @GetMapping("/user-portal")
    public String viewUserPortal(Model model) {
        model.addAttribute("payment", new PaymentDTO());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("properties", propertyRepository.findAll());
        model.addAttribute("paymentTypes", PaymentType.values());
        model.addAttribute("payments", paymentService.getAllPayments());
        return "user-portal";
    }

    @PostMapping("/user-portal/save")
    public String savePaymentFromPortal(@Valid @ModelAttribute("payment") PaymentDTO paymentDTO,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Re-populate everything to render the page with errors
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("properties", propertyRepository.findAll());
            model.addAttribute("paymentTypes", PaymentType.values());
            model.addAttribute("payments", paymentService.getAllPayments());
            model.addAttribute("showCheckout", true); // Helper flag to open checkout tab
            return "user-portal";
        }
        try {
            PaymentDTO saved = paymentService.createPayment(paymentDTO);
            if (saved.getStatus() == PaymentStatus.FAILED) {
                return "redirect:/payments/user-portal?failed=true";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("properties", propertyRepository.findAll());
            model.addAttribute("paymentTypes", PaymentType.values());
            model.addAttribute("payments", paymentService.getAllPayments());
            model.addAttribute("showCheckout", true);
            return "user-portal";
        }
        return "redirect:/payments/user-portal?success=true";
    }

    @GetMapping("/react-portal")
    public String viewReactPortal(Model model) {
        return "react-portal";
    }

    @GetMapping("/admin-portal")
    public String viewAdminPortal(Model model) {
        model.addAttribute("payment", new PaymentDTO());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("properties", propertyRepository.findAll());
        model.addAttribute("paymentTypes", PaymentType.values());
        
        List<PaymentDTO> payments = new ArrayList<>(paymentService.getAllPayments());
        Collections.reverse(payments);
        model.addAttribute("payments", payments);
        
        return "admin-portal";
    }

    @PostMapping("/admin-portal/save")
    public String savePaymentFromAdmin(@Valid @ModelAttribute("payment") PaymentDTO paymentDTO,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("properties", propertyRepository.findAll());
            model.addAttribute("paymentTypes", PaymentType.values());
            
            List<PaymentDTO> payments = new ArrayList<>(paymentService.getAllPayments());
            Collections.reverse(payments);
            model.addAttribute("payments", payments);
            
            model.addAttribute("showCheckout", true);
            return "admin-portal";
        }
        try {
            PaymentDTO saved = paymentService.createPayment(paymentDTO);
            if (saved.getStatus() == PaymentStatus.FAILED) {
                return "redirect:/payments/admin-portal?failed=true";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("properties", propertyRepository.findAll());
            model.addAttribute("paymentTypes", PaymentType.values());
            
            List<PaymentDTO> payments = new ArrayList<>(paymentService.getAllPayments());
            Collections.reverse(payments);
            model.addAttribute("payments", payments);
            
            model.addAttribute("showCheckout", true);
            return "admin-portal";
        }
        return "redirect:/payments/admin-portal?success=true";
    }
}
