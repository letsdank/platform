package net.letsdank.platform.entity.email;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.entity.common.User;

@Entity
@Table(name = "email_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailAccount {
    @Id
    @GeneratedValue(generator = "email_account_id_seq")
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(columnDefinition = "boolean default false")
    private boolean predefined;

    private String address;

    @Column(columnDefinition = "numeric(3) default 30")
    private int timeout;

    @Column(length = 100)
    private String username;

    @Column(columnDefinition = "boolean default false")
    private boolean useForSend;

    @Column(columnDefinition = "boolean default false")
    private boolean useForReceive;

    @Column(columnDefinition = "boolean default false")
    private boolean useSslForSend;

    @Column(columnDefinition = "boolean default false")
    private boolean useSslForReceive;

    @Column(columnDefinition = "boolean default false")
    private boolean saveMessageCopies;

    @Column(columnDefinition = "numeric(3) default 0")
    private int messageTtl;

    @Column(length = 100)
    private String login;

    @Column(length = 100)
    private String loginSmtp;

    @Column(columnDefinition = "numeric(5) default 0")
    private int portIncoming;

    @Column(columnDefinition = "numeric(5) default 0")
    private int portOutgoing;

    @Column(length = 4)
    private String protocol;

    @Column(length = 300)
    private String serverIncoming;

    @Column(length = 300)
    private String serverOutgoing;

    @Column(columnDefinition = "boolean default false")
    private boolean needAuthenticationBeforeSend;

    @Column(columnDefinition = "boolean default false")
    private boolean sendBbcToAccount;

    @ManyToOne
    private User owner;

    @Column(columnDefinition = "boolean default false")
    private boolean inactive;

    @Column(columnDefinition = "numeric(2) default 1")
    private int attemptCountMessageId;

    @Column(columnDefinition = "boolean using false")
    private String forSendNeedAuthentication;

    @Column(columnDefinition = "boolean default false")
    private boolean providerAuthorization;

    @Column(length = 200)
    private String providerName;
}
