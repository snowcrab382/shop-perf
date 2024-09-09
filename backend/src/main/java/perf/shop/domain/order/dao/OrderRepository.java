package perf.shop.domain.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, String> {

}
