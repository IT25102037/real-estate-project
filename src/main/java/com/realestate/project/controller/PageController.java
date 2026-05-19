package com.realestate.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/dashboard")
    public String homePage() {
        return "home";
    }

    @GetMapping("/payment-page")
    public String paymentPage() {
        return "payment";
    }

    @GetMapping("/invoice-page")
    public String invoicePage() {
        return "invoice";
    }

    @GetMapping("/payment-history-page")
    public String paymentHistoryPage() {
        return "payment-history";
    }

    @GetMapping("/bank-account-page")
    public String bankAccountPage() {
        return "bank-account";
    }

    @GetMapping("/rent-payment-page")
    public String rentPaymentPage() {
        return "rent-payment";
    }

    @GetMapping("/boost-payment-page")
    public String boostPaymentPage() {
        return "boost-payment";
    }
}
