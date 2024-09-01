package perf.shop.domain.outbox.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.outbox.domain.Outbox;
import perf.shop.domain.outbox.domain.OutboxStatus;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    Optional<Outbox> findByOrderId(String orderId);

    Long countByStatus(OutboxStatus status);
}
