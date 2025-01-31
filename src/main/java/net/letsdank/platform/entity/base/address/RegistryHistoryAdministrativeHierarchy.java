package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reg_history_administrative_hierarchy")
public class RegistryHistoryAdministrativeHierarchy {
    @Id
    private Long id;

    private Long parentId;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;

    @Column(columnDefinition = "numeric(19,0)")
    private Long recordId;

    private Long addressObject;

    private Long additionalAddressInfo;

    @Column(length = 120)
    private String name;

    private LocalDateTime startRecord;

    private LocalDateTime endRecord;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer operation;

    @Column(length = 10)
    private String objectType;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer level;
}
