package sliit.realstate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class PaymentHistoryResponse {

    private Long id;

    private Long paymentId;

    private String oldStatus;

    private String newStatus;

    private LocalDateTime updatedAt;
}