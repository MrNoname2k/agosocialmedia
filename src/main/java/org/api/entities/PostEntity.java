package org.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t1_post_entity")
@SQLDelete(sql = "UPDATE t1_post_entity SET del_flg = 1 WHERE id=?")
@Where(clause = "del_flg=0")
public class PostEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "org.api.entities.CustomIdGenerator")
    @Column(name = "id", columnDefinition = "varchar(50)")
    @JsonProperty("id")
    private String id;

    @Column(name = "content", columnDefinition = "varchar(1000)")
    @JsonProperty("content")
    private String content;

    @Column(name = "access_modifier_level")
    @JsonProperty("accessModifierLevel")
    private String accessModifierLevel;

    @Column(name = "type_post")
    @JsonProperty("typePost")
    private String typePost;

    @ManyToOne
    @JoinColumn(name = "id_user_create")
    @JsonProperty("idUserCreate")
    private UserEntity userEntityPost;

    @ManyToOne
    @JoinColumn(name = "id_album")
    @JsonProperty("idAlbum")
    private AlbumEntity albumEntityPost;

    @JsonIgnore
    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private List<LikeEntity> likes;

    @JsonIgnore
    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private List<ShareEntity> shares;

    @JsonIgnore
    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private List<FileEntity> fileEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private List<NotificationEntity> notifications;
}
