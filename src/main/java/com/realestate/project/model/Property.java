package com.realestate.project.model;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.json.GsonBuilderUtils;

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


    private String title;
    private String location;
    private double price;
    private String description;




    public Property(String title, String location , double price , String description){
        this.description = description;
        this.location = location;
        this.price = price;
        this.title = title;

    }

    public abstract String getPropertyCategory();







}


