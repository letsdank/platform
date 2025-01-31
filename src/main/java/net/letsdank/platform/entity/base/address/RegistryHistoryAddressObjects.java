package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reg_history_address_objects")
public class RegistryHistoryAddressObjects {
    @Id
    private Long id;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;


}
