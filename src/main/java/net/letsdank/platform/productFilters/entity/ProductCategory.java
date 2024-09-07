package net.letsdank.platform.productFilters.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "__product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    @Id
    private Long id;

    @Column(length = 512)
    private String name;

    @Column(length = 512)
    private String uniqueName;

    private Long parentId;

    @Column(length = 64)
    private String type;

    private Long offersNum;
    private Long modelsNum;
    private boolean visual;

    @OneToMany(mappedBy = "category")
    private List<ProductFilter> filters;
}
