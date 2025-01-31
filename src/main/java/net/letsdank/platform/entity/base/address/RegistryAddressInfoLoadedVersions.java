package net.letsdank.platform.entity.base.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reg_address_info_loaded_versions")
public class RegistryAddressInfoLoadedVersions {
    @Id
    private Long ruSubjectCode;

    @Column(columnDefinition = "numeric(10,0)")
    private Long version;

    private LocalDateTime versionDate;

    private LocalDateTime loadDate;
}
