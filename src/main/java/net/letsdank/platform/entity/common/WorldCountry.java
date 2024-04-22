package net.letsdank.platform.entity.common;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
// TODO: Nullable
public class WorldCountry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "boolean default false")
    private boolean predefined;

    @Column(length = 60)
    private String name;
    @Column(length = 3)
    private String code;

    @Column(length = 100)
    private String fullName;

    @Column(length = 2)
    private String codeAlpha2;

    @Column(length = 3)
    private String codeAlpha3;

    @Column(columnDefinition = "boolean default false")
    private boolean eaeu;

    @Column(length = 100)
    private String internationalName;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}
