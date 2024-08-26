package perf.shop.domain.product.dao;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update Product p set p.stock = p.stock - :quantity where p.id = :productId and p.stock >= :quantity")
    int deductStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
