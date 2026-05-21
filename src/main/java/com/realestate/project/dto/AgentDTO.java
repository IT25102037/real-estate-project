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

    @Pattern(regexp = "^$|^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Email format is invalid")
    private String email;

    @Size(max = 30, message = "Phone is too long")
    private String phone;

    @Size(max = 160, message = "Address is too long")
    private String address;

    @Size(max = 80, message = "Assigned area is too long")
    private String assignedArea;

    @Size(max = 20, message = "Commission rate is too long")
    private String commissionRate;

    @Pattern(regexp = "^$|^(Active|Inactive|On Leave)$", message = "Status must be Active, Inactive, or On Leave")
    private String status;

    @Size(max = 80, message = "Specialization is too long")
    private String specialization;

    @Size(max = 40, message = "Experience is too long")
    private String experience;

    @Size(max = 500, message = "Profile image URL is too long")
    private String profileImage;
}
