package net.letsdank.platform.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.entity.vetis.MeasuredUnitType;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitClassifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4)
    private String code;

    @Column(length = 25)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(columnDefinition = "boolean default false")
    private boolean predefined;

    @Column(length = 100)
    private String fullName;

    @Column(length = 3)
    private String internationalAbbr;

    @Enumerated(EnumType.STRING)
    private MeasuredUnitType measuredUnitType;
}
