package net.letsdank.platform.repository.product;

import net.letsdank.platform.entity.product.UnitClassifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitClassifierRepository extends JpaRepository<UnitClassifier, Long> {
    // TODO: Мы учитываем удаленные единицы измерения?
    Optional<UnitClassifier> findByCode(String code);
}
