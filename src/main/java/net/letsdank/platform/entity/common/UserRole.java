package net.letsdank.platform.entity.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 512)
    private String description;

    @ManyToMany
    private List<User> users;


}
