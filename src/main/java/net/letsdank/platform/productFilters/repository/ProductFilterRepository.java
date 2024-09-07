package net.letsdank.platform.productFilters.repository;

import net.letsdank.platform.productFilters.entity.ProductFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFilterRepository extends JpaRepository<ProductFilter, Long> {
}
