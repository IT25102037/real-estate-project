package com.realestate.project.service;

import com.realestate.project.dto.AgentDTO;
import com.realestate.project.model.Agent;
import com.realestate.project.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

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
                dto.getName(),
                dto.getRole(),
                dto.getSales(),
                dto.getCount(),
                dto.getPct(),
                dto.getColor()
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
            agent.setName(dto.getName());
            agent.setRole(dto.getRole());
            agent.setSales(dto.getSales());
            agent.setCount(dto.getCount());
            agent.setPct(dto.getPct());
            agent.setColor(dto.getColor());
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
}
