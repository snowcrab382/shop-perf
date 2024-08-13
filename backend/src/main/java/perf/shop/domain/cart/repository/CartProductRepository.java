package perf.shop.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.cart.domain.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
