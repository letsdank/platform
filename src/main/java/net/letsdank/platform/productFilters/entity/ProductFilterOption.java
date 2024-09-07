package net.letsdank.platform.productFilters.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "__product_filter_option")
@Getter
@Setter
public class ProductFilterOption {
    @Id
    private Long id;

    @ManyToOne
    private ProductFilter filter;

    private String name;

    @Column(columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> values;
}
