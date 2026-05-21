package sliit.realstate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WalletController {

    @GetMapping("/wallet")
    public String walletPage() {
        return "home";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
