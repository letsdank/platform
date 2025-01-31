package net.letsdank.platform.entity.base.address;

import jakarta.persistence.*;

@Entity
@Table(name = "reg_address_objects")
public class RegistryAddressObjects {
    @Id
    private Long id;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;

    @Column(length = 120)
    private String name;

    @Column(length = 10)
    private String objectType;

    @Column(columnDefinition = "numeric(17,0)")
    private Long kladrCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private RegistryAdditionalAddressInfo additionalAddressInfo;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer level;
}
