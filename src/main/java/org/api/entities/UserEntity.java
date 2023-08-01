package org.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "t1_user_entity")
@SQLDelete(sql = "UPDATE t1_user_entity SET del_flg = 1 WHERE id=?")
@Where(clause = "del_flg=0")
public class  UserEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "org.api.entities.CustomIdGenerator")
    @Column(name = "id", columnDefinition = "varchar(50)")
    @JsonProperty("id")
    private String id;

    @Column(name = "first_name")
    @JsonProperty("firstName")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("lastName")
    private String lastName;

    @Column(name = "phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name = "mail")
    @JsonProperty("mail")
    private String mail;

    @Column(name = "address")
    @JsonProperty("address")
    private String address;

    @Column(name = "city")
    @JsonProperty("city")
    private String city;

    @Column(name = "birthDay")
    @JsonProperty("birthDay")
    private Date birthDay;

    @Column(name = "gender")
    @JsonProperty("gender")
    private String gender;

    @Column(name = "password")
    @JsonProperty("password")
    private String password;

    @Column(name = "link_ig")
    @JsonProperty("linkIg")
    private String linkIg;

    @Column(name = "link_facebook")
    @JsonProperty("linkFacebook")
    private String linkFacebook;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "last_login_date")
    @JsonProperty("lastLoginDate")
    private Date lastLoginDate;

    @Column(name = "online", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonProperty("online")
    private Boolean online;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;

    @Column(name = "code")
    @JsonProperty("code")
    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityPost", cascade = CascadeType.ALL)
    private List<PostEntity> posts;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityLike", cascade = CascadeType.ALL)
    private List<LikeEntity> likes;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityShare", cascade = CascadeType.ALL)
    private List<ShareEntity> shares;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityComment", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityFrom", cascade = CascadeType.ALL)
    private List<MessageEntity> messageFrom;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityTo", cascade = CascadeType.ALL)
    private List<MessageEntity> messageTo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name="t1_users_roles",
            joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
    private Set<UserRoleEntity> authorities;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityOne", cascade = CascadeType.ALL)
    private List<RelationshipEntity> relationshipEntitiesOne;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntityTow", cascade = CascadeType.ALL)
    private List<RelationshipEntity> relationshipEntitiesTow;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<NotificationEntity> notifications;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<AlbumEntity> albumEntities;

    @Override
    public String toString() {
        return getMail();
    }

    public String getFullName() {
        return this.lastName + ' ' + this.firstName;
    }
}
