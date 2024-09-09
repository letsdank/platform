package net.letsdank.platform.module.email.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailMessageAddress {
    private String email;
    private String name;
}
