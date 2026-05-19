package sliit.realstate.dto;

import lombok.Data;

@Data
public class CreateBoostPaymentRequest {

    private Long userId;

    private Long propertyId;

    private Long paymentId;

    private String boostPackage;

    private Integer boostDays;
}