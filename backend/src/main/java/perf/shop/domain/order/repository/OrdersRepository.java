package perf.shop.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.order.domain.Order;

public interface OrdersRepository extends JpaRepository<Order, String> {
}
