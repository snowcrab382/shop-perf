package perf.shop.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrderId(String orderId);
}
