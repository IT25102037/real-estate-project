package com.realestate.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index", "/index.html"})
    public String showBuyPage() {
        return "Index";
    }

    @GetMapping("/rent")
    public String showRentPage() {
        return "Rent";
    }

    @GetMapping("/sell")
    public String showSellPage() {
        return "Sell";
    }

    @GetMapping({"/agents", "/agents.html"})
    public String showAgentsPage() {
        return "Agents";
    }
}
