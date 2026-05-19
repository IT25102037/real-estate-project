package sliit.realstate.dto;

import lombok.Builder;
import lombok.Data;
import sliit.realstate.enums.BoostStatus;

import java.time.LocalDate;

@Data
@Builder
public class BoostPaymentResponse {

    private Long id;

    private Long userId;

    private Long propertyId;

    private Long paymentId;

    private String boostPackage;

    private Integer boostDays;

    private LocalDate startDate;

    private LocalDate endDate;

    private BoostStatus status;
}