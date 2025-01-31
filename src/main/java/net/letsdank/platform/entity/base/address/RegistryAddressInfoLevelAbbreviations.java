package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reg_address_info_level_abbreviations")
public class RegistryAddressInfoLevelAbbreviations {
    @Id
    @Column(length = 20)
    private String abbreviation;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer level;

    @Column(length = 100)
    private String value;
}
