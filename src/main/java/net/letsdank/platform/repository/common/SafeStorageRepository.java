package net.letsdank.platform.repository.common;

import net.letsdank.platform.entity.common.SafeStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SafeStorageRepository extends JpaRepository<SafeStorageEntity, UUID> {
    @Query("SELECT s.value FROM SafeStorageEntity s WHERE s.tableName =?1 AND s.entityId =?2 AND s.key =?3")
    Optional<String> getByKey(String tableName, Long entityId, String key);
}
