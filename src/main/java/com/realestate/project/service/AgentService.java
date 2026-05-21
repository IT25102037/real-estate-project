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
        return agentRepository.findAll().stream()
                .map(this::enrichAgentDefaults)
                .sorted((left, right) -> Double.compare(moneyToNumber(right.getSales()), moneyToNumber(left.getSales())))
                .toList();
    }

    public Optional<Agent> getAgentById(Long id) {
        return agentRepository.findById(id).map(this::enrichAgentDefaults);
    }

    private Agent enrichAgentDefaults(Agent agent) {
        AgentDTO defaults = new AgentDTO();
        defaults.setEmail(agent.getEmail());
        defaults.setPhone(agent.getPhone());
        defaults.setAddress(agent.getAddress());
        defaults.setAssignedArea(agent.getAssignedArea());
        defaults.setCommissionRate(agent.getCommissionRate());
        defaults.setStatus(agent.getStatus());
        defaults.setSpecialization(agent.getSpecialization());
        defaults.setExperience(agent.getExperience());
        defaults.setProfileImage(agent.getProfileImage());
        applyExtendedFields(agent, defaults);
        return agent;
    }

    public Agent createAgent(AgentDTO dto) {
        Agent agent = new Agent(
                dto.getName().trim(),
                dto.getRole(),
                normalizeMoney(dto.getSales()),
                normalizeDeals(dto.getCount())
        );
        applyExtendedFields(agent, dto);
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
            applyExtendedFields(agent, dto);
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

    private double moneyToNumber(String value) {
        if (value == null) {
            return 0.0;
        }

        Matcher matcher = MONEY_PATTERN.matcher(value.trim());
        if (!matcher.matches()) {
            return 0.0;
        }

        double amount = Double.parseDouble((matcher.group(1) + (matcher.group(2) == null ? "" : matcher.group(2))).replace(",", ""));
        String suffix = matcher.group(3) == null ? "" : matcher.group(3).toUpperCase();
        if ("M".equals(suffix)) {
            return amount * 1_000_000.0;
        }
        if ("K".equals(suffix)) {
            return amount * 1_000.0;
        }
        return amount;
    }

    private void applyExtendedFields(Agent agent, AgentDTO dto) {
        agent.setEmail(trimOrDefault(dto.getEmail(), deriveEmail(agent.getName())));
        agent.setPhone(trimOrDefault(dto.getPhone(), "—"));
        agent.setAddress(trimOrDefault(dto.getAddress(), "—"));
        agent.setAssignedArea(trimOrDefault(dto.getAssignedArea(), agent.getRole()));
        agent.setCommissionRate(trimOrDefault(dto.getCommissionRate(), "5%"));
        agent.setStatus(trimOrDefault(dto.getStatus(), "Active"));
        agent.setSpecialization(trimOrDefault(dto.getSpecialization(), agent.getRole()));
        agent.setExperience(trimOrDefault(dto.getExperience(), "—"));
        agent.setProfileImage(trimOrDefault(dto.getProfileImage(), defaultProfileImage(agent.getId())));
    }

    private String trimOrDefault(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }

    private String deriveEmail(String name) {
        String slug = name == null ? "agent" : name.toLowerCase().replaceAll("[^a-z0-9]+", ".");
        return slug + "@luxeestate.com";
    }

    private String defaultProfileImage(Long id) {
        long seed = id != null ? id : 1L;
        return "https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=200&sig=" + seed;
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
