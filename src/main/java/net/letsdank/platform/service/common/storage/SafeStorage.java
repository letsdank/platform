package net.letsdank.platform.service.common.storage;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.common.SafeStorageEntity;
import net.letsdank.platform.repository.common.SafeStorageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис работы с безопасным хранилищем.
 */
@Service
@AllArgsConstructor
public class SafeStorage {

    private final EntityManager entityManager;
    private final SafeStorageRepository repository;
    private final SafeStorageCrypto crypto;

    public String getValue(Class<?> entityClass, Long entityId, String key) {
        if (!hasEntityAnnotation(entityClass)) {
            throw new IllegalArgumentException("Entity class must be annotated with @Entity");
        }

        // 1) Получить из Entity класса название таблицы в БД
        String tableName = getTableName(entityManager, entityClass);

        // 2) Получить значение из БД
        Optional<SafeStorageEntity> rawValue = repository.getByKey(tableName, entityId, key);

        // 3) Расшифровать значение
        return rawValue.map(entity -> crypto.decrypt(entity.getValue())).orElse(null);

    }

    public void setValue(Class<?> entityClass, Long entityId, String key, String value) {
        if (!hasEntityAnnotation(entityClass)) {
            throw new IllegalArgumentException("Entity class must be annotated with @Entity");
        }

        // 1) Получить из Entity класса название таблицы в БД
        String tableName = getTableName(entityManager, entityClass);

        // 2) Зашифровать значение
        String encryptedValue = crypto.encrypt(value);

        // 3) Сохранить значение в БД
        Optional<SafeStorageEntity> oldValue = repository.getByKey(tableName, entityId, key);
        if (oldValue.isPresent()) {
            SafeStorageEntity entity = oldValue.get();
            entity.setValue(encryptedValue);
            repository.save(entity);

            return;
        }

        // Если значение пустое, то создаем новый Entity
        SafeStorageEntity safeStorageEntity = new SafeStorageEntity();

        safeStorageEntity.setTableName(tableName);
        safeStorageEntity.setEntityId(entityId);
        safeStorageEntity.setKey(key);
        safeStorageEntity.setValue(encryptedValue);

        repository.save(safeStorageEntity);
    }

    private boolean hasEntityAnnotation(Class<?> entityClass) {
        // Проверяем наличие аннотации @Entity у класса
        return entityClass.isAnnotationPresent(Entity.class);
    }

    private <T> String getTableName(EntityManager entityManager, Class<T> entityClass) {
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<T> entityType = metamodel.entity(entityClass);

        Table table = entityClass.getAnnotation(Table.class);

        // Так как мы используем Postgres, то название таблиц формируется
        // по правилу snake_case, т.е. нам нужно сделать преобразование от
        // обычного entityType.getName() в snake_case.
        String tableName;
        if (table == null)
            tableName = String.join("_", entityType.getName()
                    .split("(?=\\p{Upper})"));
        else tableName = table.name();

        return tableName;
    }
}
