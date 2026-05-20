package com.realestate.project.service;

import com.realestate.project.dto.AgentDTO;
import com.realestate.project.model.Agent;
import com.realestate.project.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AgentService {
    private static final Pattern MONEY_PATTERN = Pattern.compile(
            "^[Rr][Ss]\\.?\\s?(\\d{1,3}(?:,\\d{3})+|\\d+)(\\.\\d{1,2})?\\s?([KkMm])?$"
    );
    private static final Pattern DEAL_COUNT_PATTERN = Pattern.compile("^(\\d{1,4})");

    private final AgentRepository agentRepository;
    private final ActivityService activityService;

    public AgentService(AgentRepository agentRepository, ActivityService activityService) {
        this.agentRepository = agentRepository;
        this.activityService = activityService;
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public Optional<Agent> getAgentById(Long id) {
        return agentRepository.findById(id);
    }

    public Agent createAgent(AgentDTO dto) {
        Agent agent = new Agent(
                dto.getName().trim(),
                dto.getRole(),
                normalizeMoney(dto.getSales()),
                normalizeDeals(dto.getCount())
        );
        Agent saved = agentRepository.save(agent);
        activityService.logActivity(
                "AGENT_CREATED",
                "<strong>Agent added</strong> — " + saved.getName() + " (" + saved.getRole() + ")",
                "user-plus",
                "blue"
        );
        return saved;
    }

    public Optional<Agent> updateAgent(Long id, AgentDTO dto) {
        return agentRepository.findById(id).map(agent -> {
            agent.setName(dto.getName().trim());
            agent.setRole(dto.getRole());
            agent.setSales(normalizeMoney(dto.getSales()));
            agent.setCount(normalizeDeals(dto.getCount()));
            Agent saved = agentRepository.save(agent);
            activityService.logActivity(
                    "AGENT_UPDATED",
                    "<strong>Agent updated</strong> — " + saved.getName() + " details updated",
                    "pencil",
                    "blue"
            );
            return saved;
        });
    }

    public boolean deleteAgent(Long id) {
        return agentRepository.findById(id).map(agent -> {
            agentRepository.delete(agent);
            activityService.logActivity(
                    "AGENT_DELETED",
                    "<strong>Agent removed</strong> — " + agent.getName(),
                    "trash-2",
                    "red"
            );
            return true;
        }).orElse(false);
    }

    private String normalizeMoney(String value) {
        Matcher matcher = MONEY_PATTERN.matcher(value.trim());
        if (!matcher.matches()) {
            return value.trim();
        }

        String amount = matcher.group(1).replace(",", "");
        String decimal = matcher.group(2) == null ? "" : matcher.group(2);
        String suffix = matcher.group(3) == null ? "" : matcher.group(3).toUpperCase();
        return "Rs " + amount + decimal + suffix;
    }

    private String normalizeDeals(String count) {
        Matcher matcher = DEAL_COUNT_PATTERN.matcher(count.trim());
        if (!matcher.find()) {
            return count.trim();
        }

        int deals = Integer.parseInt(matcher.group(1));
        return deals + (deals == 1 ? " deal" : " deals");
    }
}
