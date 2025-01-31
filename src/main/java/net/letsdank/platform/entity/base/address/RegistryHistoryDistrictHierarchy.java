package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reg_history_district_hierarchy")
public class RegistryHistoryDistrictHierarchy {
    @Id
    private Long id;

    private Long parentId;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;

    @Column(columnDefinition = "numeric(19,0)")
    private Long recordId;
}
