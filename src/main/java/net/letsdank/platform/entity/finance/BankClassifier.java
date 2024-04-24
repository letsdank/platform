package net.letsdank.platform.entity.finance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.entity.common.WorldCountry;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankClassifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 9)
    private String code;

    @Column(length = 100)
    private String name;

    @ManyToOne
    private BankClassifier parent;

    @Column(columnDefinition = "boolean default false")
    private boolean isGroup;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(columnDefinition = "boolean default false")
    private boolean predefined;

    @Column(length = 20)
    private String corrAccount;

    @Column(length = 50)
    private String city;

    @Column(length = 500)
    private String address;

    @Column(length = 250)
    private String phones;

    @Column(columnDefinition = "boolean default false")
    private boolean inactive;

    @Column(length = 11)
    private String swiftBic;

    @Column(length = 12)
    private String inn;

    @ManyToOne
    private WorldCountry country;

    @Column(length = 100)
    private String internationalName;

    @Column(length = 50)
    private String internationalCity;

    @Column(length = 500)
    private String internationalAddress;

    @ManyToOne
    private BankClassifier bicRkc;

    @Column(length = 15)
    private String cbInternalCode;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    private List<BankClassifier> children;
}
