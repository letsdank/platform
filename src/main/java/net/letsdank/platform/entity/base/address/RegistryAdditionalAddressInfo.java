package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reg_additional_address_info")
public class RegistryAdditionalAddressInfo {
    @Id
    private Long id;

    @Column(columnDefinition = "numeric(2,0)")
    private Integer ruSubjectCode;

    @Column(columnDefinition = "numeric(10,0)")
    private Long postalCode;

    @Column(columnDefinition = "numeric(11,0)")
    private Long oktmo;

    @Column(columnDefinition = "numeric(11,0)")
    private Long okato;

    @Column(columnDefinition = "numeric(4,0)")
    private Integer plotCodeIfnsul;

    @Column(columnDefinition = "numeric(4,0)")
    private Integer plotCodeIfnsfl;

    @Column(columnDefinition = "numeric(4,0)")
    private Integer codeIfnsul;

    @Column(columnDefinition = "numeric(4,0)")
    private Integer codeIfnsfl;
}
