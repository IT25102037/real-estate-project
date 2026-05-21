package com.realestate.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index"})
    public String showBuyPage() {
        return "forward:/index.html";
    }

    @GetMapping("/rent")
    public String showRentPage() {
        return "forward:/rent.html";
    }

    @GetMapping("/sell")
    public String showSellPage() {
        return "forward:/sell.html";
    }

    @GetMapping("/agents")
    public String showAgentsPage() {
        return "forward:/agents.html";
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "forward:/contact.html";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "forward:/about.html";
    }

    @GetMapping("/reports")
    public String showReportsPage() {
        return "forward:/reports.html";
    }
}
