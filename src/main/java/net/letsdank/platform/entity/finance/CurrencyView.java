package net.letsdank.platform.entity.finance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyView {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Currency currency;

    @Column(length = 10)
    private String langCode;

    @Column(length = 200)
    private String signatureParameters;
}
