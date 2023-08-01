package org.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "t1_user_role_entity")
public class UserRoleEntity extends CommonEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "org.api.entities.CustomIdGenerator")
    @Column(name = "id", columnDefinition = "varchar(50)")
    @JsonProperty("id")
    private String id;

    private String authority;

    public UserRoleEntity() {
    }

    @Column(name = "authority", nullable = false)
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
