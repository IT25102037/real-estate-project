package com.realestate.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.realestate.project.dto.BoostPaymentResponse;
import com.realestate.project.dto.CreateBoostPaymentRequest;
import com.realestate.project.entity.BoostPayment;
import com.realestate.project.enums.BoostStatus;
import com.realestate.project.repository.BoostPaymentRepository;
import com.realestate.project.service.BoostPaymentService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoostPaymentServiceImpl
        implements BoostPaymentService {

    private final BoostPaymentRepository boostPaymentRepository;

    @Override
    public BoostPaymentResponse createBoostPayment(
            CreateBoostPaymentRequest request
    ) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(
                request.getBoostDays()
        );

        BoostPayment boostPayment = BoostPayment.builder()
                .userId(request.getUserId())
                .propertyId(request.getPropertyId())
                .paymentId(request.getPaymentId())
                .boostPackage(request.getBoostPackage())
                .boostDays(request.getBoostDays())
                .startDate(startDate)
                .endDate(endDate)
                .status(BoostStatus.ACTIVE)
                .build();

        BoostPayment saved =
                boostPaymentRepository.save(boostPayment);

        return mapToResponse(saved);
    }

    @Override
    public List<BoostPaymentResponse>
    getAllBoostPayments() {

        return boostPaymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BoostPaymentResponse expireBoostPayment(Long id) {

        BoostPayment boostPayment =
                boostPaymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Boost payment not found"
                                )
                        );

        boostPayment.setStatus(BoostStatus.EXPIRED);

        BoostPayment updated =
                boostPaymentRepository.save(boostPayment);

        return mapToResponse(updated);
    }

    private BoostPaymentResponse mapToResponse(
            BoostPayment boostPayment
    ) {

        return BoostPaymentResponse.builder()
                .id(boostPayment.getId())
                .userId(boostPayment.getUserId())
                .propertyId(boostPayment.getPropertyId())
                .paymentId(boostPayment.getPaymentId())
                .boostPackage(boostPayment.getBoostPackage())
                .boostDays(boostPayment.getBoostDays())
                .startDate(boostPayment.getStartDate())
                .endDate(boostPayment.getEndDate())
                .status(boostPayment.getStatus())
                .build();
    }
    @Override
    public List<BoostPaymentResponse> autoExpireBoosts() {

        return boostPaymentRepository.findAll()
                .stream()
                .filter(boost ->
                        boost.getStatus() == BoostStatus.ACTIVE
                                && boost.getEndDate()
                                .isBefore(LocalDate.now())
                )
                .map(boost -> {

                    boost.setStatus(
                            BoostStatus.EXPIRED
                    );

                    BoostPayment updated =
                            boostPaymentRepository.save(boost);

                    return mapToResponse(updated);

                }).toList();
    }
}
