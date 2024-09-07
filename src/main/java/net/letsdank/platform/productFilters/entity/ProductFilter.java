package net.letsdank.platform.productFilters.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "__product_filter")
@Getter
@Setter
public class ProductFilter {
    @Id
    private Long id;

    @ManyToOne
    private ProductCategory category;

    @Column(length = 512)
    private String name;

    private boolean isCharacteristic;

    @Column(length = 64)
    private String type;

    @Column(length = 64)
    private String subtype;

    @Column(length = 64)
    private String unit;

    private boolean exactly;

    @Column(columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> optionTypes;

    @OneToMany(mappedBy = "filter")
    private List<ProductFilterOption> options;
}
