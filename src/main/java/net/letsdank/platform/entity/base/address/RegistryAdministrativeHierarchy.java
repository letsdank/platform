package net.letsdank.platform.entity.base.address;

import jakarta.persistence.*;

@Entity
@Table(name = "reg_administrative_hierarchy")
public class RegistryAdministrativeHierarchy {
    @Id
    private Long id;

    @ManyToOne
    private RegistryAdministrativeHierarchy parent;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;
}
