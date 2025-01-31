package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reg_internal_address_info")
public class RegistryInternalAddressInfo {
    @Column(length = 20)
    private String type;

    @Id
    private Long id;

    @Column(length = 20)
    private String key;

    @Column(length = 300)
    private String value;
}
