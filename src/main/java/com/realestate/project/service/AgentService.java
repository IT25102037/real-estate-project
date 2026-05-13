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

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
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
        return agentRepository.save(agent);
    }

    public Optional<Agent> updateAgent(Long id, AgentDTO dto) {
        return agentRepository.findById(id).map(agent -> {
            agent.setName(dto.getName());
            agent.setRole(dto.getRole());
            agent.setSales(dto.getSales());
            agent.setCount(dto.getCount());
            agent.setPct(dto.getPct());
            agent.setColor(dto.getColor());
            return agentRepository.save(agent);
        });
    }

    public boolean deleteAgent(Long id) {
        if (agentRepository.existsById(id)) {
            agentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
