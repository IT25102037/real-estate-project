package com.realestate.project.controller;

import com.realestate.project.model.Agent;
import com.realestate.project.service.AgentService;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/agents")
public class AgentController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public String viewAgentsPage(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "status", required = false) String status,
                                 Model model) {

        model.addAttribute("agentList", agentService.filterAgents(keyword, status));
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "agents";
    }

    @GetMapping("/new")
    public String showAddAgentForm(Model model) {
        model.addAttribute("agent", new Agent());
        return "add-agent";
    }

    @PostMapping("/save")
    public String saveAgent(@ModelAttribute("agent") Agent agent,
                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
            agent.setProfileImageName(fileName);
        }

        agentService.saveAgent(agent);
        return "redirect:/agents";
    }

    @GetMapping("/edit/{id}")
    public String showEditAgentForm(@PathVariable Long id, Model model) {
        model.addAttribute("agent", agentService.getAgentById(id));
        return "edit-agent";
    }

    @PostMapping("/update/{id}")
    public String updateAgent(@PathVariable Long id,
                              @ModelAttribute("agent") Agent agent,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Agent existingAgent = agentService.getAgentById(id);

        existingAgent.setName(agent.getName());
        existingAgent.setEmail(agent.getEmail());
        existingAgent.setPhone(agent.getPhone());
        existingAgent.setAddress(agent.getAddress());
        existingAgent.setAssignedArea(agent.getAssignedArea());
        existingAgent.setCommissionRate(agent.getCommissionRate());
        existingAgent.setStatus(agent.getStatus());
        existingAgent.setSpecialization(agent.getSpecialization());
        existingAgent.setExperience(agent.getExperience());

        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
            existingAgent.setProfileImageName(fileName);
        }

        agentService.saveAgent(existingAgent);
        return "redirect:/agents";
    }

    @GetMapping("/delete/{id}")
    public String deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return "redirect:/agents";
    }

    @GetMapping("/profile/{id}")
    public String viewAgentProfile(@PathVariable Long id,
                                   @RequestParam(value = "from", required = false) String from,
                                   Model model) {

        model.addAttribute("agent", agentService.getAgentById(id));

        if ("admin".equals(from)) {
            model.addAttribute("backUrl", "/agents");
        } else {
            model.addAttribute("backUrl", "/agents/active");
        }

        return "agent-profile";
    }

    @GetMapping("/active")
    public String viewActiveAgents(Model model) {
        model.addAttribute("agentList", agentService.getActiveAgents());
        return "customer-agents";
    }
}