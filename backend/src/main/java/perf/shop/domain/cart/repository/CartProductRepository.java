package perf.shop.domain.cart.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.cart.domain.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    void deleteAllByCartId(Long cartId);

    Optional<CartProduct> findByIdAndCartId(Long id, Long cartId);
}
