package com.realestate.project.repository;

import com.realestate.project.model.Agent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    List<Agent> findByNameContainingIgnoreCase(String name);

    List<Agent> findByStatus(String status);
}