package sliit.realstate.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sliit.realstate.entity.RentPayment;
import sliit.realstate.enums.RentStatus;

import java.util.List;

public interface RentPaymentRepository
        extends JpaRepository<RentPayment, Long> {

    List<RentPayment> findByStatus(
            RentStatus status
    );
}