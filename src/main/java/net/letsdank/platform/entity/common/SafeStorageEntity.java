package net.letsdank.platform.entity.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "safe_storage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SafeStorageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 255)
    private String tableName;

    @Column
    private Long entityId;

    @Column(length = 1024)
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;
}
