package com.realestate.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgentDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Sales is required")
    private String sales;

    @NotBlank(message = "Count is required")
    private String count;

    @NotNull(message = "Percentage is required")
    private Integer pct;

    @NotBlank(message = "Color is required")
    private String color;
}
