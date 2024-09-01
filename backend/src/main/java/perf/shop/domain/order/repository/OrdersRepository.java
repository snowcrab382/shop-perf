package perf.shop.domain.order.repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import perf.shop.domain.order.domain.Order;

public interface OrdersRepository extends JpaRepository<Order, String> {

    @Query(value = "SELECT p.created_at as paid, o.created_at as ordered, " +
            "TIMESTAMPDIFF(SECOND, o.created_at, p.created_at) AS difference " +
            "FROM orders o " +
            "LEFT JOIN payment p ON o.id = p.order_id " +
            "ORDER BY o.created_at DESC " +
            "LIMIT 100", nativeQuery = true)
    List<Map<String, Object>> findOrderAndPaymentTimeDifference();

}
