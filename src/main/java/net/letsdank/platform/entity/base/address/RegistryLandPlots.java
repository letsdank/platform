package net.letsdank.platform.entity.base.address;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "reg_land_plots")
public class RegistryLandPlots {
    @Id
    private Long addressObject;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private RegistryAdditionalAddressInfo additionalAddressInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> plots;
}
