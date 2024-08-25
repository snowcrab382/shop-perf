package perf.shop.domain.order.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import perf.shop.domain.order.domain.Order;

public interface OrdersRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE o.orderer.userId = :userId")
    List<Order> findAllByUserId(Long userId);
}
