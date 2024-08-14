package perf.shop.domain.cart.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.cart.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

}
