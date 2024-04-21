package net.letsdank.platform.entity.finance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.entity.common.WorldCountry;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// TODO: Nullable
public class Bank {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 9)
    private String code;

    @Column(length = 150)
    private String name;

    @ManyToOne
    private Bank parent;

    private boolean isGroup;

    @Column(columnDefinition = "boolean default false")
    private boolean predefined;

    @Column(length = 20)
    private String corrAccount;

    @Column(length = 50)
    private String city;

    private String address;
    private String phones;
    private int manualModification;

    @Column(length = 11)
    private String swiftBic;

    @ManyToOne
    private WorldCountry country;
}
