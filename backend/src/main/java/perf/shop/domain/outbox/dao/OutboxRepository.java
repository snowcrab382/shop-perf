package perf.shop.domain.outbox.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.outbox.domain.Outbox;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    Optional<Outbox> findByOrderId(String orderId);

}
