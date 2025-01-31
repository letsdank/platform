package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "reg_address_info_edit_reasons")
public class RegistryAddressInfoEditReasons {
    @Id
    private Long id;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;

    private Long editedObject;

    @Column(columnDefinition = "boolean default false")
    private Boolean containsDescription;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> description;
}
