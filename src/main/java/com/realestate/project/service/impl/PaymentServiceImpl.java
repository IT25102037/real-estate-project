package com.realestate.project.service.impl;

import com.realestate.project.dto.PaymentDTO;
import com.realestate.project.entity.Payment;
import com.realestate.project.entity.Property;
import com.realestate.project.entity.User;
import com.realestate.project.exception.ResourceNotFoundException;
import com.realestate.project.repo.PaymentRepository;
import com.realestate.project.repo.PropertyRepository;
import com.realestate.project.repo.UserRepository;
import com.realestate.project.service.PaymentService;
import com.realestate.project.service.strategy.PaymentMethod;
import com.realestate.project.service.strategy.PaymentStrategyFactory;
import com.realestate.project.util.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PaymentStrategyFactory strategyFactory;

    @Override
    public PaymentDTO createPayment(PaymentDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + dto.getUserId()));
        
        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + dto.getPropertyId()));

        Payment payment = PaymentMapper.toEntity(dto);
        payment.setUser(user);
        payment.setProperty(property);
        payment.setDate(LocalDate.now());

        // Use Strategy Pattern to process payment
        PaymentMethod paymentMethod = strategyFactory.getPaymentMethod(payment.getPaymentType());
        paymentMethod.processPayment(payment);

        payment = paymentRepository.save(payment);
        return PaymentMapper.toDTO(payment);
    }

    @Override
    public PaymentDTO updatePayment(Long id, PaymentDTO dto) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));

        existingPayment.setAmount(dto.getAmount());
        existingPayment.setPaymentType(dto.getPaymentType());
        
        if (!existingPayment.getUser().getId().equals(dto.getUserId())) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + dto.getUserId()));
            existingPayment.setUser(user);
        }
        
        if (!existingPayment.getProperty().getId().equals(dto.getPropertyId())) {
            Property property = propertyRepository.findById(dto.getPropertyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + dto.getPropertyId()));
            existingPayment.setProperty(property);
        }

        PaymentMethod paymentMethod = strategyFactory.getPaymentMethod(existingPayment.getPaymentType());
        paymentMethod.processPayment(existingPayment);

        Payment savedPayment = paymentRepository.save(existingPayment);
        return PaymentMapper.toDTO(savedPayment);
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
        return PaymentMapper.toDTO(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
        paymentRepository.delete(payment);
    }

    @Override
    public List<PaymentDTO> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
