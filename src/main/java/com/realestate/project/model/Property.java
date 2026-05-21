package com.realestate.project.model;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = House.class, name = "house"),
        @JsonSubTypes.Type(value = Apartment.class, name = "apartment"),
        @JsonSubTypes.Type(value = RentalProperty.class, name = "rental")
})
public abstract class Property{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Property title is required")
    @Size(min = 2, max = 100, message = "Property title must be 2 to 100 characters")
    private String title;

    @NotBlank(message = "Address is required")
    @Size(min = 2, max = 160, message = "Address must be 2 to 160 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .,'#&()/\\-]{1,159}$",
            message = "Address contains invalid characters")
    private String address;

    @NotBlank(message = "District is required")
    @Pattern(regexp = "^(Ampara|Anuradhapura|Badulla|Batticaloa|Colombo|Galle|Gampaha|Hambantota|Jaffna|Kalutara|Kandy|Kegalle|Kilinochchi|Kurunegala|Mannar|Matale|Matara|Monaragala|Mullaitivu|Nuwara Eliya|Polonnaruwa|Puttalam|Ratnapura|Trincomalee|Vavuniya)$",
            message = "District is invalid")
    private String district;

    private double price;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 1000, message = "Description must be 2 to 1000 characters")
    private String description;




    public Property(String title, String address, String district, double price, String description){
        this.description = description;
        this.address = address;
        this.district = district;
        this.price = price;
        this.title = title;

    }

    public abstract String getPropertyCategory();





}
