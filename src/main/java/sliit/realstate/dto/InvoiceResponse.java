package sliit.realstate.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder

public class InvoiceResponse {

    private Long invoiceId;

    private String invoiceNumber;

    private BigDecimal totalAmount;

    private LocalDateTime issuedDate;

    private Long paymentId;
}