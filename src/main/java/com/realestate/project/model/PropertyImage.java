package com.realestate.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PropertyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    public PropertyImage(String fileName, String fileType, byte[] data, Property property) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.property = property;
    }
}
