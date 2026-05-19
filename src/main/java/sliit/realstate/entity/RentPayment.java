package sliit.realstate.entity;

import jakarta.persistence.*;
import lombok.*;
import sliit.realstate.enums.RentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rent_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long propertyId;

    private String rentMonth;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    private LocalDate dueDate;

    private LocalDateTime paidDate;
}