package perf.shop.domain.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import perf.shop.domain.product.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
