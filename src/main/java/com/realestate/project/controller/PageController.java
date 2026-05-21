package com.realestate.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index", "/index.html"})
    public String showBuyPage() {
        return "Index";
    }

    @GetMapping({"/rent", "/rent.html"})
    public String showRentPage() {
        return "rent";
    }

    @GetMapping({"/sell", "/sell.html"})
    public String showSellPage() {
        return "sell";
    }

    @GetMapping({"/agents", "/agents.html"})
    public String showAgentsPage() {
        return "Agents";
    }

    @GetMapping({"/contact", "/contact.html"})
    public String showContactPage(){
        return "Contact";
    }

    @GetMapping({"/about" , "/about.html"})
    public String showAboutPage(){
        return "about";
    }

    @GetMapping("/reports")
    public String showReportsPage() {
        return "forward:/reports.html";
    }
}
