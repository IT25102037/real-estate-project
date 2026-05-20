package com.realestate.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String activityType;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String icon;

    @Column(nullable = false)
    private String colorClass;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public ActivityLog(String activityType, String description, String icon, String colorClass) {
        this.activityType = activityType;
        this.description = description;
        this.icon = icon;
        this.colorClass = colorClass;
        this.timestamp = LocalDateTime.now();
    }
}
