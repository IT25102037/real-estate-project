package sliit.realstate.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sliit.realstate.entity.BoostPayment;

public interface BoostPaymentRepository
        extends JpaRepository<BoostPayment, Long> {
}