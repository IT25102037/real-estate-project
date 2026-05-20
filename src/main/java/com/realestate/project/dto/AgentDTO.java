package com.realestate.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AgentDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 70, message = "Name must be 2 to 70 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z .'-]{1,69}$",
            message = "Name must contain only letters, spaces, apostrophes, hyphens, or periods")
    private String name;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(Senior Agent|Luxury Specialist|Agent|Junior Agent|Other)$",
            message = "Role is invalid")
    private String role;

    @NotBlank(message = "Sales is required")
    @Size(max = 20, message = "Sales amount is too long")
    @Pattern(regexp = "^[Rr][Ss]\\.?\\s?(\\d{1,3}(,\\d{3})+|\\d+)(\\.\\d{1,2})?\\s?([KkMm])?$",
            message = "Sales amount must use Rs format, e.g. Rs 12.4M")
    private String sales;

    @NotBlank(message = "Count is required")
    @Pattern(regexp = "^(0|[1-9][0-9]{0,3})(\\sdeal(s)?)?$",
            message = "Deals count must be a whole number from 0 to 9999")
    private String count;
}
