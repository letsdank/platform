package net.letsdank.platform.entity.finance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 3)
    private String code;

    @Column(length = 10)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean predefined;

    @Column(columnDefinition = "boolean default false")
    private boolean loadedFromInternet;

    @Column(length = 50)
    private String fullName;

    @Column(columnDefinition = "numeric(10,2)")
    private double markup;

    @ManyToOne
    private Currency baseCurrency;

    @Column(length = 200)
    private String signatureParameters;

    @Column(length = 3)
    private String symbolRepresentation;

    @Column(length = 100)
    private String exchangeRateCalc;

    @Enumerated(EnumType.STRING)
    private ExchangeRateSettingMethod exchangeRateSetting;

    @OneToMany(mappedBy = "currency")
    private List<CurrencyView> views;
}
