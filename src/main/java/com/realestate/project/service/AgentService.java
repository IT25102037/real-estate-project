package com.realestate.project.service;

import com.realestate.project.model.Agent;
import com.realestate.project.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public void saveAgent(Agent agent) {

        boolean isNewAgent = agent.getAgentId() == null;

        Agent savedAgent = agentRepository.save(agent);

        if (isNewAgent && (savedAgent.getAgentCode() == null || savedAgent.getAgentCode().isEmpty())) {
            String code = String.format("AG%03d", savedAgent.getAgentId());
            savedAgent.setAgentCode(code);
            agentRepository.save(savedAgent);
        }
    }
    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + id));
    }

    public void deleteAgent(Long id) {
        agentRepository.deleteById(id);
    }

    public List<Agent> getActiveAgents() {
        return agentRepository.findByStatus("Active");
    }

    public List<Agent> filterAgents(String keyword, String status) {

        List<Agent> agents = agentRepository.findAll();

        if (keyword != null && !keyword.isEmpty()) {
            agents = agents.stream()
                    .filter(a -> a.getName().toLowerCase().contains(keyword.toLowerCase())
                            || (a.getAgentCode() != null && a.getAgentCode().toLowerCase().contains(keyword.toLowerCase())))
                    .toList();
        }

        if (status != null && !status.isEmpty()) {
            agents = agents.stream()
                    .filter(a -> a.getStatus().equalsIgnoreCase(status))
                    .toList();
        }

        return agents;
    }
}