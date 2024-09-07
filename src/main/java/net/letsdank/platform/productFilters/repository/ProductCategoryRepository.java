package net.letsdank.platform.productFilters.repository;

import net.letsdank.platform.productFilters.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findAllByParentId(Long parentId);
}
