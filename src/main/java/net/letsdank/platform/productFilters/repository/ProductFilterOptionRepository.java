package net.letsdank.platform.productFilters.repository;

import net.letsdank.platform.productFilters.entity.ProductFilterOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFilterOptionRepository extends JpaRepository<ProductFilterOption, Long> {
}
