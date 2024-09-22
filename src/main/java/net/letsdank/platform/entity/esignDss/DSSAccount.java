package net.letsdank.platform.entity.esignDss;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.entity.common.User;

@Entity
@Table(name = "dss_account")
@Getter
@Setter
@NoArgsConstructor
public class DSSAccount {
    @Id
    private Long id;

    @Column(length = 100)
    private String name;

    @ManyToOne
    private User owner;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(columnDefinition = "boolean deafult false")
    private boolean predefined;

    @Column(length = 100)
    private String predefinedName;

    @ManyToOne
    private User author;

    @Enumerated(EnumType.STRING)
    private DSSAuthMethod primaryAuth;

    @Enumerated(EnumType.STRING)
    private DSSAuthMethod secondaryAuth;

    @Column(columnDefinition = "text")
    private String additionalInfo;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(length = 50)
    private String login;

    @Column(columnDefinition = "jsonb")
    private String applicationInfo;

    @Enumerated(EnumType.STRING)
    private DSSAccountApplicationStatus applicationStatus;
}
