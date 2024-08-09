package perf.shop.domain.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import perf.shop.domain.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
