package sliit.realstate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.realstate.dto.CreateRentPaymentRequest;
import sliit.realstate.dto.RentPaymentResponse;
import sliit.realstate.entity.RentPayment;
import sliit.realstate.enums.RentStatus;
import sliit.realstate.repo.RentPaymentRepository;
import sliit.realstate.service.RentPaymentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentPaymentServiceImpl
        implements RentPaymentService {

    private final RentPaymentRepository
            rentPaymentRepository;

    @Override
    public RentPaymentResponse createRentPayment(
            CreateRentPaymentRequest request
    ) {

        RentPayment rentPayment = RentPayment.builder()
                .userId(request.getUserId())
                .propertyId(request.getPropertyId())
                .rentMonth(request.getRentMonth())
                .amount(request.getAmount())
                .dueDate(request.getDueDate())
                .status(RentStatus.PENDING)
                .build();

        RentPayment saved =
                rentPaymentRepository.save(rentPayment);

        return mapToResponse(saved);
    }

    @Override
    public List<RentPaymentResponse>
    getAllRentPayments() {

        return rentPaymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public RentPaymentResponse markAsPaid(Long id) {

        RentPayment rentPayment =
                rentPaymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Rent payment not found"
                                )
                        );

        rentPayment.setStatus(RentStatus.PAID);
        rentPayment.setPaidDate(LocalDateTime.now());

        RentPayment updated =
                rentPaymentRepository.save(rentPayment);

        return mapToResponse(updated);
    }

    private RentPaymentResponse mapToResponse(
            RentPayment rentPayment
    ) {

        return RentPaymentResponse.builder()
                .id(rentPayment.getId())
                .userId(rentPayment.getUserId())
                .propertyId(rentPayment.getPropertyId())
                .rentMonth(rentPayment.getRentMonth())
                .amount(rentPayment.getAmount())
                .status(rentPayment.getStatus())
                .dueDate(rentPayment.getDueDate())
                .paidDate(rentPayment.getPaidDate())
                .build();
    }
    @Override
    public List<RentPaymentResponse> markOverdueRents() {

        return rentPaymentRepository.findAll()
                .stream()
                .filter(rent ->
                        rent.getStatus() == RentStatus.PENDING
                                && rent.getDueDate()
                                .isBefore(LocalDate.now())
                )
                .map(rent -> {
                    rent.setStatus(RentStatus.OVERDUE);
                    RentPayment updated =
                            rentPaymentRepository.save(rent);

                    return mapToResponse(updated);
                })
                .toList();
    }
    @Override
    public List<RentPaymentResponse> getRentPaymentsByStatus(
            RentStatus status
    ) {
        return rentPaymentRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}