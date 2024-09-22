package net.letsdank.platform.entity.esignDss;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "dss_server")
@Getter
@Setter
@NoArgsConstructor
public class DSSServer {
    @Id
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(columnDefinition = "boolean default false")
    private boolean predefined;

    @Column(length = 100)
    private String predefinedName;

    @Column(length = 200, nullable = false)
    private String address;

    @Column(columnDefinition = "numeric(4,0)")
    private Integer timeout;

    @Column(length = 30)
    private String confirmTemplate;

    @Column(length = 50, nullable = false)
    // Идентификатор ЦИ (Центр Идентификации)
    private String icId;

    @Column(length = 100, nullable = false)
    private String sapId;

    @Column(length = 250)
    private String auditService;

    @Column(length = 250, nullable = false)
    private String identificationService;

    @Column(length = 250)
    private String docProcessingService;

    @Column(length = 250, nullable = false)
    private String signService;

    @Column(length = 200)
    private String validateService;

    @Column(length = 10, nullable = false)
    private String apiVersion;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(columnDefinition = "boolean default true")
    private boolean using;

    @Column(length = 250, nullable = false)
    private String cabinetAddress;

    @Column
    private Long internalId;

    @OneToMany(mappedBy = "server")
    private List<DSSServerAuthRestriction> authRestrictions;
}
