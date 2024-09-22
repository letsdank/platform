package net.letsdank.platform.entity.esignDss;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dss_server_auth_restriction")
@Getter
@Setter
@NoArgsConstructor
public class DSSServerAuthRestriction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private DSSServer server;

    @Enumerated(EnumType.STRING)
    private DSSAuthMethod authMethod;
}
